package de.erwinho.klausurplaner.dto;
import java.util.List;

public record SemesterUebersicht(
        int aktuellesSemester,
        int erreichteEcts,
        List<ModulUebersicht> module
) {
}
