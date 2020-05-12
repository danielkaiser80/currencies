package de.danielkaiser.currencies.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import de.danielkaiser.currencies.dto.CurrencyDto;

@Service
public class CurrencyLoader {

    private static final String FILE_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip";
    private static final String FILE_NAME = "eurofxref";
    private static final String ZIP_FILE_NAME = FILE_NAME + ".zip";
    private static final String CSV_FILE_NAME = FILE_NAME + ".csv";

    private final Logger log = LogManager.getLogger();

    private Map<String, Double> currencies = new HashMap<>();

    private LocalDate retrievalDate = LocalDate.now();

    @NonNull
    public List<CurrencyDto> getAllCurrencies() {
        if (currenciesNeedToBeUpdated()) {
            log.info("New currencies needed, retrieving...");
            loadCurrenciesFromEcb();
        }

        return currencies.entrySet().stream().map(entry -> CurrencyDto.from(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    private boolean currenciesNeedToBeUpdated() {
        return currencies.isEmpty() || retrievalDate.isBefore(LocalDate.now());
    }

    public void loadCurrenciesFromEcb() {

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(FILE_URL).openStream());
                        FileOutputStream fileOutputStream = new FileOutputStream(ZIP_FILE_NAME)) {

            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            Unzipper.unzipFile(ZIP_FILE_NAME);
            buildMapFromFile();

        } catch (IOException e) {
            log.error(String.format("Could not retrieve ZIP file from %s", FILE_URL), e);
        }

    }

    private void buildMapFromFile() {

        try (Stream<String> stream = Files.lines(Paths.get(CSV_FILE_NAME))) {

            final List<List<String>> currencyValues = stream.map(s -> s.split(",")).map(Arrays::asList).collect(Collectors.toList());
            final List<String> isoCodes = currencyValues.get(0);

            // first is date and last one is empty
            currencies = IntStream.range(1, isoCodes.size() - 1).boxed().collect(Collectors.toMap(isoCodes::get, index -> {
                final String s = currencyValues.get(1).get(index);
                return Double.parseDouble(s);
            }));

            retrievalDate = LocalDate.now();

        } catch (IOException e) {
            log.error(String.format("Could not read CSV file from %s", CSV_FILE_NAME), e);
        }

    }
}
