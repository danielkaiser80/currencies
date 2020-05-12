package de.danielkaiser.currencies.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.danielkaiser.currencies.dto.CurrencyDto;

@Service
public class CurrencyLoader {

    private Map<String, Double> currencies = new HashMap<>();


    public List<CurrencyDto> getAllCurrencies() {
        return currencies.entrySet().stream().map(entry -> CurrencyDto.from(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }
}
