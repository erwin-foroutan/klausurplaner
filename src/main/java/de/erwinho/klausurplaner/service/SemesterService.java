package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.dto.ModulUebersicht;
import de.erwinho.klausurplaner.dto.SemesterUebersicht;
import de.erwinho.klausurplaner.entity.Modul;
import de.erwinho.klausurplaner.entity.ModulStatus;
import de.erwinho.klausurplaner.entity.Pruefungsanmeldung;
import de.erwinho.klausurplaner.repository.ModulRepository;
import de.erwinho.klausurplaner.repository.PruefungsanmeldungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterService {

    private final StudienprofilService studienprofilService;
    private final ModulRepository modulRepository;
    private final PruefungsanmeldungRepository anmeldungRepository;

    public SemesterUebersicht aktuellesSemester() {
        int aktuell = studienprofilService.aktuellesSemester();
        List<ModulUebersicht> alle = alleModule();

        int erreichteEcts = alle.stream()
                .filter(u -> u.status() == ModulStatus.BESTANDEN)
                .mapToInt(ModulUebersicht::ects)
                .sum();

        List<ModulUebersicht> relevant = alle.stream()
                .filter(u -> u.semester() == aktuell
                        || (u.semester() < aktuell && u.status() != ModulStatus.BESTANDEN))
                .toList();

        return new SemesterUebersicht(aktuell, erreichteEcts, relevant);
    }

    public List<ModulUebersicht> verlauf() {
        return alleModule();
    }

    private List<ModulUebersicht> alleModule() {
        Map<Long, List<Pruefungsanmeldung>> anmeldungenProModul = anmeldungRepository.findAllMitModul().stream()
                .collect(Collectors.groupingBy(a -> a.getModul().getId()));

        return modulRepository.findAll().stream()
                .map(m -> uebersicht(m, anmeldungenProModul.getOrDefault(m.getId(), List.of())))
                .sorted(Comparator.comparingInt(ModulUebersicht::semester)
                        .thenComparing(ModulUebersicht::modulId))
                .toList();
    }

    private ModulUebersicht uebersicht(Modul modul, List<Pruefungsanmeldung> anmeldungen) {
        return new ModulUebersicht(
                modul.getId(),
                modul.getName(),
                modul.getSemester(),
                modul.getEcts(),
                modul.getKategorie(),
                PruefungsService.bestimmeStatus(anmeldungen),
                PruefungsService.fehlversuche(anmeldungen),
                PruefungsService.istLetzterVersuch(anmeldungen),
                PruefungsService.bestandeneNote(anmeldungen),
                PruefungsService.offeneAnmeldungId(anmeldungen)
        );
    }
}