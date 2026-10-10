package de.erwinho.klausurplaner.dto;

import de.erwinho.klausurplaner.entity.PruefungsStatus;
import jakarta.validation.constraints.NotNull;

public record ErgebnisRequest(
        @NotNull(message = "status fehlt") PruefungsStatus status,
        Double note
) {
}