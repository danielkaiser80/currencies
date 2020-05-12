package de.danielkaiser.currencies.rest;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.danielkaiser.currencies.dto.CurrencyDto;
import de.danielkaiser.currencies.service.CurrencyLoader;

@RestController
@RequestMapping(value = "/currencies", produces = APPLICATION_JSON_VALUE)
public class CurrencyController {

    private final Logger log = LogManager.getLogger();

    private final CurrencyLoader currencyLoader;

    public CurrencyController(CurrencyLoader currencyLoader) {
        this.currencyLoader = currencyLoader;
    }

    @GetMapping
    public List<CurrencyDto> getAllInsuranceConnections() {
        log.info("REST request to get all currencies");
        return currencyLoader.getAllCurrencies();
    }

    // TODO GET currencies/SYMBOL
    // param validation
}
