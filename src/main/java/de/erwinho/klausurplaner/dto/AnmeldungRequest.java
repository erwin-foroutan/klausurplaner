package de.erwinho.klausurplaner.dto;
import java.time.LocalDate;

public record AnmeldungRequest(Long modulId, LocalDate datum) {
}
