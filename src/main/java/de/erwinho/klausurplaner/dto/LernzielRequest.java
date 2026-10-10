package de.erwinho.klausurplaner.dto;
import java.time.LocalDate;

public record LernzielRequest(
        Long modulId, int zielStunden, LocalDate startDatum, LocalDate deadline, String beschreibung
) {
}
