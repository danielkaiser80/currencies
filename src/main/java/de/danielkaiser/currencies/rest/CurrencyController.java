package de.danielkaiser.currencies.rest;

import de.danielkaiser.currencies.dto.CurrencyDto;
import de.danielkaiser.currencies.service.CurrencyLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * REST controller to expose the currencies.
 */
@RestController
@RequestMapping(value = "/currencies", produces = APPLICATION_JSON_VALUE)
@Log4j2
@RequiredArgsConstructor
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
