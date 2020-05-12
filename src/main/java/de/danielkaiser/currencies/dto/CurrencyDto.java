package de.danielkaiser.currencies.dto;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A simple DTO, comprising of an ISO code and a currency value.
 */
@Getter
@RequiredArgsConstructor(staticName = "from")
public class CurrencyDto {

    @NonNull
    private final String isoCode;

    @NonNull
    private final Double value;

}
