package de.erwinho.klausurplaner.dto;

import de.erwinho.klausurplaner.entity.PruefungsStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NachtragRequest(
        @NotNull(message = "modulId fehlt") Long modulId,
        @NotNull(message = "datum fehlt") LocalDate datum,
        @NotNull(message = "status fehlt") PruefungsStatus status,
        Double note
) {
}