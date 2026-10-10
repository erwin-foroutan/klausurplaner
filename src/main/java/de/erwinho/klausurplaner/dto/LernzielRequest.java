package de.erwinho.klausurplaner.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record LernzielRequest(
        @NotNull(message = "modulId fehlt") Long modulId,
        @Min(value = 1, message = "zielStunden muss mindestens 1 sein") int zielStunden,
        @NotNull(message = "startDatum fehlt") LocalDate startDatum,
        @NotNull(message = "deadline fehlt") LocalDate deadline,
        @Size(max = 255, message = "beschreibung darf höchstens 255 Zeichen lang sein") String beschreibung
) {
}