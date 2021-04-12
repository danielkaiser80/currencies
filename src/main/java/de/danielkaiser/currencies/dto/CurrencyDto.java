package de.danielkaiser.currencies.dto;

import lombok.Value;

/**
 * A simple DTO, comprising of an ISO code and a currency value.
 */
@Value(staticConstructor = "from")
public class CurrencyDto {

    String isoCode;

    Double value;

}
