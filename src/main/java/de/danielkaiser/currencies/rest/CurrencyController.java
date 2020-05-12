package de.danielkaiser.currencies.rest;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.danielkaiser.currencies.dto.CurrencyDto;

@RestController
@RequestMapping(value = "/currencies/", produces = APPLICATION_JSON_VALUE)
public class CurrencyController {

    private final Logger log = LogManager.getLogger();

    @GetMapping
    public List<CurrencyDto> getAllInsuranceConnections() {
        log.debug("REST request to get all currencies");
        return Collections.emptyList();
    }

    // TODO GET currencies/SYMBOL
    // param validation
}
