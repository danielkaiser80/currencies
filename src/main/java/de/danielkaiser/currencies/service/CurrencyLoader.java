package de.danielkaiser.currencies.service;

import de.danielkaiser.currencies.dto.CurrencyDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Service to load and hold the currency values in memory.
 */
@Service
@Log4j2
public class CurrencyLoader {

    private static final String FILE_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip";
    private static final String FILE_NAME = "eurofxref";
    private static final String ZIP_FILE_NAME = FILE_NAME + ".zip";
    private static final String CSV_FILE_NAME = FILE_NAME + ".csv";

    private List<CurrencyDto> currencies = Collections.emptyList();

    private LocalDate retrievalDate = LocalDate.now();

    public List<CurrencyDto> getAllCurrencies() {
        if (currenciesNeedToBeUpdated()) {
            log.info("New currencies needed, retrieving...");
            loadCurrenciesFromEcb();
        }

        return currencies;
    }

    public Optional<CurrencyDto> getCurrency(final String isoCode) {
        if (currenciesNeedToBeUpdated()) {
            log.info("New currencies needed, retrieving...");
            loadCurrenciesFromEcb();
        }
        return currencies.stream().filter(currencyDto -> currencyDto.getIsoCode().equalsIgnoreCase(isoCode)).findFirst();
    }

    private boolean currenciesNeedToBeUpdated() {
        return currencies.isEmpty() || retrievalDate.isBefore(LocalDate.now());
    }

    private void loadCurrenciesFromEcb() {

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(FILE_URL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(ZIP_FILE_NAME)) {

            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            UnzipUtil.unzipFile(ZIP_FILE_NAME);

            final Path path = Paths.get(CSV_FILE_NAME);
            Assert.isTrue(Files.exists(path), () -> "CSV file did not exist in ZIP archive.");
            buildCurrenyListFromFile(path);

        } catch (IOException e) {
            log.error(String.format("Could not retrieve ZIP file from %s", FILE_URL), e);
        }

    }

    private void buildCurrenyListFromFile(final Path path) {

        try (Stream<String> stream = Files.lines(path)) {

            final List<List<String>> currencyValues = stream.map(s -> s.split(",")).map(Arrays::asList).collect(Collectors.toList());
            final List<String> isoCodes = currencyValues.get(0);

            // first is date and last one is empty
            currencies = IntStream.range(1, isoCodes.size() - 1).boxed()
                    .map(integer -> CurrencyDto.from(isoCodes.get(integer).trim(), getCurrencyValueForIndex(currencyValues, integer)))
                    .collect(Collectors.toList());

            retrievalDate = LocalDate.now();

        } catch (IOException e) {
            log.error(String.format("Could not read CSV file from %s", CSV_FILE_NAME), e);
        }

    }

    private Double getCurrencyValueForIndex(List<List<String>> currencyValues, int index) {
        final String s = currencyValues.get(1).get(index);
        return Double.parseDouble(s);
    }
}
