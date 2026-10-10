package de.erwinho.klausurplaner.dto;
import de.erwinho.klausurplaner.entity.PruefungsStatus;
public record ErgebnisRequest(PruefungsStatus status, Double note) {
}
