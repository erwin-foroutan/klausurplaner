package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.dto.LernzielResponse;
import de.erwinho.klausurplaner.entity.Lerneinheit;
import de.erwinho.klausurplaner.entity.Lernziel;
import de.erwinho.klausurplaner.entity.Modul;
import de.erwinho.klausurplaner.repository.BenutzerRepository;
import de.erwinho.klausurplaner.repository.LerneinheitRepository;
import de.erwinho.klausurplaner.repository.LernzielRepository;
import de.erwinho.klausurplaner.repository.ModulRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LernService {

    private final LerneinheitRepository lerneinheitRepository;
    private final LernzielRepository lernzielRepository;
    private final ModulRepository modulRepository;
    private final BenutzerRepository benutzerRepository;

    // ---------- Lerneinheiten ----------

    public Lerneinheit lerneinheitErfassen(Long benutzerId, Long modulId, LocalDate datum,
                                           int dauerMinuten, String thema, String notiz) {
        if (dauerMinuten <= 0) {
            throw new IllegalArgumentException("Die Dauer muss größer als 0 Minuten sein");
        }
        Lerneinheit einheit = new Lerneinheit();
        einheit.setBenutzer(benutzerRepository.getReferenceById(benutzerId));
        einheit.setModul(findeModul(modulId));
        einheit.setDatum(datum != null ? datum : LocalDate.now());
        einheit.setDauerMinuten(dauerMinuten);
        einheit.setThema(thema);
        einheit.setNotiz(notiz);
        return lerneinheitRepository.save(einheit);
    }

    public List<Lerneinheit> lerneinheiten(Long benutzerId, Long modulId) {
        return lerneinheitRepository.findByModulIdAndBenutzerIdOrderByDatumDesc(modulId, benutzerId);
    }

    // ---------- Lernziele ----------

    public Lernziel lernzielAnlegen(Long benutzerId, Long modulId, int zielStunden,
                                    LocalDate startDatum, LocalDate deadline, String beschreibung) {
        pruefeLernziel(zielStunden, startDatum, deadline);
        Lernziel ziel = new Lernziel();
        ziel.setBenutzer(benutzerRepository.getReferenceById(benutzerId));
        ziel.setModul(findeModul(modulId));
        ziel.setZielStunden(zielStunden);
        ziel.setStartDatum(startDatum);
        ziel.setDeadline(deadline);
        ziel.setBeschreibung(beschreibung);
        return lernzielRepository.save(ziel);
    }

    public List<Lernziel> lernziele(Long benutzerId, Long modulId) {
        return lernzielRepository.findByModulIdAndBenutzerId(modulId, benutzerId);
    }

    public int gelernteMinuten(Lernziel ziel) {
        return summeMinuten(lerneinheitRepository.findByModulIdAndBenutzerIdAndDatumBetween(
                ziel.getModul().getId(), ziel.getBenutzer().getId(), ziel.getStartDatum(), ziel.getDeadline()));
    }

    public LernzielResponse alsResponse(Lernziel ziel) {
        int minuten = gelernteMinuten(ziel);
        long tage = Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), ziel.getDeadline()));
        return new LernzielResponse(
                ziel.getId(),
                ziel.getModul().getId(),
                ziel.getModul().getName(),
                ziel.getZielStunden(),
                ziel.getStartDatum(),
                ziel.getDeadline(),
                ziel.getBeschreibung(),
                minuten,
                fortschrittProzent(minuten, ziel.getZielStunden()),
                tage);
    }

    private Modul findeModul(Long modulId) {
        return modulRepository.findById(modulId)
                .orElseThrow(() -> new IllegalArgumentException("Modul mit ID " + modulId + " nicht gefunden"));
    }

    // ---------- Regeln & Berechnungen ----------

    static int summeMinuten(List<Lerneinheit> einheiten) {
        return einheiten.stream()
                .mapToInt(Lerneinheit::getDauerMinuten)
                .sum();
    }

    static int fortschrittProzent(int gelernteMinuten, int zielStunden) {
        int prozent = gelernteMinuten * 100 / (zielStunden * 60);
        return Math.min(prozent, 100);
    }

    static void pruefeLernziel(int zielStunden, LocalDate startDatum, LocalDate deadline) {
        if (zielStunden <= 0) {
            throw new IllegalArgumentException("Die Zielstunden müssen größer als 0 sein");
        }
        if (startDatum == null || deadline == null) {
            throw new IllegalArgumentException("Startdatum und Deadline müssen angegeben werden");
        }
        if (deadline.isBefore(startDatum)) {
            throw new IllegalArgumentException("Die Deadline darf nicht vor dem Startdatum liegen");
        }
    }
}