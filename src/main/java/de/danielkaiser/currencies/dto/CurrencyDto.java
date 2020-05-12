package de.danielkaiser.currencies.dto;

import org.springframework.lang.NonNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "from")
public class CurrencyDto {

    @NonNull
    private final String isoCode;

    @NonNull
    private final Double value;

}
