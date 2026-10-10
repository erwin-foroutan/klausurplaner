package de.erwinho.klausurplaner.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record StudienprofilRequest(
        @Min(value = 2000, message = "startJahr ist unrealistisch")
        @Max(value = 2100, message = "startJahr ist unrealistisch") int startJahr
) {
}