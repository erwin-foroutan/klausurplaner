package de.erwinho.klausurplaner.dto;
import de.erwinho.klausurplaner.entity.PruefungsStatus;
import java.time.LocalDate;
public record NachtragRequest(Long modulId, LocalDate datum, PruefungsStatus status, Double note) {
}
