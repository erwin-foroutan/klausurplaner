package de.erwinho.klausurplaner.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AnmeldungRequest(
        @NotNull(message = "modulId fehlt") Long modulId,
        @NotNull(message = "datum fehlt") LocalDate datum
) {
}