package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.entity.Modul;
import de.erwinho.klausurplaner.entity.ModulStatus;
import de.erwinho.klausurplaner.entity.Pruefungsanmeldung;
import de.erwinho.klausurplaner.entity.PruefungsStatus;
import de.erwinho.klausurplaner.repository.BenutzerRepository;
import de.erwinho.klausurplaner.repository.ModulRepository;
import de.erwinho.klausurplaner.repository.PruefungsanmeldungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PruefungsService {

    static final int MAX_VERSUCHE = 3;

    private final PruefungsanmeldungRepository anmeldungRepository;
    private final ModulRepository modulRepository;
    private final BenutzerRepository benutzerRepository;

    public Pruefungsanmeldung anmelden(Long benutzerId, Long modulId, LocalDate datum) {
        Modul modul = modulRepository.findById(modulId)
                .orElseThrow(() -> new IllegalArgumentException("Modul mit ID " + modulId + " nicht gefunden"));

        List<Pruefungsanmeldung> bisher = anmeldungRepository.findByModulIdAndBenutzerId(modulId, benutzerId);
        pruefeAnmeldungErlaubt(bisher);

        Pruefungsanmeldung anmeldung = new Pruefungsanmeldung();
        anmeldung.setBenutzer(benutzerRepository.getReferenceById(benutzerId));
        anmeldung.setModul(modul);
        anmeldung.setDatum(datum);
        anmeldung.setStatus(PruefungsStatus.ANGEMELDET);
        return anmeldungRepository.save(anmeldung);
    }

    public Pruefungsanmeldung ergebnisEintragen(Long benutzerId, Long anmeldungId, PruefungsStatus status, Double note) {
        Pruefungsanmeldung anmeldung = anmeldungRepository.findByIdAndBenutzerId(anmeldungId, benutzerId)
                .orElseThrow(() -> new IllegalArgumentException("Anmeldung mit ID " + anmeldungId + " nicht gefunden"));
        setzeErgebnis(anmeldung, status, note);
        return anmeldungRepository.save(anmeldung);
    }

    @Transactional
    public Pruefungsanmeldung nachtragen(Long benutzerId, Long modulId, LocalDate datum, PruefungsStatus status, Double note) {
        Pruefungsanmeldung anmeldung = anmelden(benutzerId, modulId, datum);
        setzeErgebnis(anmeldung, status, note);
        return anmeldungRepository.save(anmeldung);
    }

    @Transactional
    public void letzteRueckgaengig(Long benutzerId, Long modulId) {
        Pruefungsanmeldung letzte = anmeldungRepository.findByModulIdAndBenutzerId(modulId, benutzerId).stream()
                .max(Comparator.comparing(Pruefungsanmeldung::getId))
                .orElseThrow(() -> new IllegalStateException("Für dieses Modul gibt es nichts rückgängig zu machen"));
        anmeldungRepository.delete(letzte);
    }

    // ---------- Regeln ----------

    static long fehlversuche(List<Pruefungsanmeldung> anmeldungen) {
        return anmeldungen.stream()
                .filter(a -> a.getStatus() == PruefungsStatus.NICHT_BESTANDEN)
                .count();
    }

    static boolean istBestanden(List<Pruefungsanmeldung> anmeldungen) {
        return anmeldungen.stream()
                .anyMatch(a -> a.getStatus() == PruefungsStatus.BESTANDEN);
    }

    static boolean istEndgueltigNichtBestanden(List<Pruefungsanmeldung> anmeldungen) {
        return fehlversuche(anmeldungen) >= MAX_VERSUCHE;
    }

    static boolean istLetzterVersuch(List<Pruefungsanmeldung> anmeldungen) {
        return !istBestanden(anmeldungen) && fehlversuche(anmeldungen) == MAX_VERSUCHE - 1;
    }

    static void pruefeAnmeldungErlaubt(List<Pruefungsanmeldung> anmeldungen) {
        if (istBestanden(anmeldungen)) {
            throw new IllegalStateException("Modul ist bereits bestanden");
        }
        if (istEndgueltigNichtBestanden(anmeldungen)) {
            throw new IllegalStateException("Endgültig nicht bestanden – keine weitere Anmeldung möglich");
        }
        boolean offeneAnmeldung = anmeldungen.stream()
                .anyMatch(a -> a.getStatus() == PruefungsStatus.ANGEMELDET);
        if (offeneAnmeldung) {
            throw new IllegalStateException("Es gibt bereits eine offene Anmeldung für dieses Modul");
        }
    }

    static void setzeErgebnis(Pruefungsanmeldung anmeldung, PruefungsStatus status, Double note) {
        if (anmeldung.getStatus() != PruefungsStatus.ANGEMELDET) {
            throw new IllegalStateException("Für diese Anmeldung wurde bereits ein Ergebnis eingetragen");
        }
        switch (status) {
            case BESTANDEN -> {
                if (note == null || note < 1.0 || note > 4.0) {
                    throw new IllegalArgumentException("Bei bestanden muss die Note zwischen 1,0 und 4,0 liegen");
                }
                anmeldung.setNote(note);
            }
            case NICHT_BESTANDEN -> anmeldung.setNote(5.0);
            case NICHT_ANGETRETEN -> anmeldung.setNote(null);
            case ANGEMELDET -> throw new IllegalArgumentException("ANGEMELDET ist kein Ergebnis");
        }
        anmeldung.setStatus(status);
    }

    static ModulStatus bestimmeStatus(List<Pruefungsanmeldung> anmeldungen) {
        if (istBestanden(anmeldungen)) {
            return ModulStatus.BESTANDEN;
        }
        if (istEndgueltigNichtBestanden(anmeldungen)) {
            return ModulStatus.ENDGUELTIG_NICHT_BESTANDEN;
        }
        boolean offeneAnmeldung = anmeldungen.stream()
                .anyMatch(a -> a.getStatus() == PruefungsStatus.ANGEMELDET);
        if (offeneAnmeldung) {
            return ModulStatus.ANGEMELDET;
        }
        return ModulStatus.OFFEN;
    }

    static Double bestandeneNote(List<Pruefungsanmeldung> anmeldungen) {
        return anmeldungen.stream()
                .filter(a -> a.getStatus() == PruefungsStatus.BESTANDEN)
                .map(Pruefungsanmeldung::getNote)
                .findFirst()
                .orElse(null);
    }

    static Long offeneAnmeldungId(List<Pruefungsanmeldung> anmeldungen) {
        return anmeldungen.stream()
                .filter(a -> a.getStatus() == PruefungsStatus.ANGEMELDET)
                .map(Pruefungsanmeldung::getId)
                .findFirst()
                .orElse(null);
    }
}