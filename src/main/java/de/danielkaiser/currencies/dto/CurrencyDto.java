package de.danielkaiser.currencies.dto;

/**
 * A simple DTO, comprising of an ISO code and a currency value.
 */
public record CurrencyDto(String isoCode, Double value) {
}
