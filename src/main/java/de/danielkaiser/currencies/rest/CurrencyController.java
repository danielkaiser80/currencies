package de.danielkaiser.currencies.rest;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.danielkaiser.currencies.dto.CurrencyDto;
import de.danielkaiser.currencies.service.CurrencyLoader;

/**
 * REST controller to expose the currencies.
 */
@RestController
@RequestMapping(value = "/currencies", produces = APPLICATION_JSON_VALUE)
public class CurrencyController {

    private final Logger log = LogManager.getLogger();

    private final CurrencyLoader currencyLoader;

    public CurrencyController(CurrencyLoader currencyLoader) {
        this.currencyLoader = currencyLoader;
    }

    @GetMapping
    public List<CurrencyDto> getAllCurrencies() {
        log.info("REST request to get all currencies");
        return currencyLoader.getAllCurrencies();
    }

    @GetMapping("/{SYMBOL}")
    public ResponseEntity<CurrencyDto> getOneCurrency(@NonNull @PathVariable final String SYMBOL) {
        if (SYMBOL.length() != 3) {
            log.info("An ISO code must have three letters {}", SYMBOL);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("REST request to get currency value for {}", SYMBOL);
        return ResponseEntity.of(currencyLoader.getCurrency(SYMBOL));
    }
}
