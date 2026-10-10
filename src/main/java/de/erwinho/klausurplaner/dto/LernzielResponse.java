package de.erwinho.klausurplaner.dto;
import java.time.LocalDate;
public record LernzielResponse(
        Long id,
        Long modulId,
        String modulName,
        int zielStunden,
        LocalDate startDatum,
        LocalDate deadline,
        String beschreibung,
        int gelernteMinuten,
        int fortschrittProzent,
        long verbleibendeTage
) {
}
