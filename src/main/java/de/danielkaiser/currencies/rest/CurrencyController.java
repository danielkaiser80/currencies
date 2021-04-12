package de.danielkaiser.currencies.rest;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.danielkaiser.currencies.dto.CurrencyDto;
import de.danielkaiser.currencies.service.CurrencyLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * REST controller to expose the currencies.
 */
@RestController
@RequestMapping(value = "/currencies", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class CurrencyController {

    private final CurrencyLoader currencyLoader;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<CurrencyDto> getAllCurrencies() {
        log.info("REST request to get all currencies");
        return currencyLoader.getAllCurrencies();
    }

    @GetMapping("/{SYMBOL}")
    public ResponseEntity<CurrencyDto> getOneCurrency(@PathVariable final String SYMBOL) {
        if (SYMBOL.length() != 3) {
            log.info("An ISO code must have three letters {}", SYMBOL);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("REST request to get currency value for {}", SYMBOL);
        return ResponseEntity.of(currencyLoader.getCurrency(SYMBOL));
    }
}
