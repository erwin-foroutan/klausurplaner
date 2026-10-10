package de.erwinho.klausurplaner.dto;
import de.erwinho.klausurplaner.entity.Lerneinheit;
import java.time.LocalDate;
public record LerneinheitResponse(
        Long id,
        Long modulId,
        String modulName,
        LocalDate datum,
        int dauerMinuten,
        String thema,
        String notiz
) {
    public static LerneinheitResponse von(Lerneinheit e) {
        return new LerneinheitResponse(
                e.getId(),e.getModul().getId(),e.getModul().getName(),
                e.getDatum(),e.getDauerMinuten(),e.getThema(),e.getNotiz()
        );
    }
}
