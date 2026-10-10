package de.erwinho.klausurplaner.dto;
import de.erwinho.klausurplaner.entity.Pruefungsanmeldung;
import de.erwinho.klausurplaner.entity.PruefungsStatus;
import java.time.LocalDate;


public record AnmeldungResponse(
        Long id,
        Long modulId,
        String modulName,
        LocalDate datum,
        PruefungsStatus status,
        Double note
) {
    public static AnmeldungResponse von(Pruefungsanmeldung a) {
        return new AnmeldungResponse(
                a.getId(),
                a.getModul().getId(),
                a.getModul().getName(),
                a.getDatum(),
                a.getStatus(),
                a.getNote()
        );
    }
}
