package de.erwinho.klausurplaner.config;

import de.erwinho.klausurplaner.entity.Modul;
import de.erwinho.klausurplaner.repository.ModulRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static de.erwinho.klausurplaner.entity.Kategorie.*;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ModulRepository modulRepository;

    @Override
    public void run(String... args) {
        if (modulRepository.count() > 0) {
            return;
        }
        modulRepository.saveAll(List.of(
                // 1. Semester
                new Modul("Einführung in die Informatik", 5, 1, INFORMATIK_GRUNDLAGEN),
                new Modul("Objektorientierte Softwareentwicklung", 10, 1, INFORMATIK_GRUNDLAGEN),
                new Modul("IT-Recht und Datenschutz", 5, 1, FACHUEBERGREIFEND),
                new Modul("Diskrete Strukturen", 5, 1, MATHEMATIK),
                new Modul("Angewandte Mathematik", 5, 1, MATHEMATIK),
                // 2. Semester
                new Modul("Algorithmen und Datenstrukturen", 10, 2, INFORMATIK_GRUNDLAGEN),
                new Modul("Programmiermethoden", 5, 2, INFORMATIK_GRUNDLAGEN),
                new Modul("Programmierparadigmen", 5, 2, INFORMATIK_GRUNDLAGEN),
                new Modul("Datenbanken", 5, 2, INFORMATIK_ANWENDUNGEN),
                new Modul("Lineare Algebra", 5, 2, MATHEMATIK),
                // 3. Semester
                new Modul("Rechnernetze", 5, 3, INFORMATIK_GRUNDLAGEN),
                new Modul("Softwaretechnik", 10, 3, INFORMATIK_ANWENDUNGEN),
                new Modul("Computergrafik", 5, 3, INFORMATIK_ANWENDUNGEN),
                new Modul("Web und IoT", 5, 3, INFORMATIK_ANWENDUNGEN),
                new Modul("Statistik und Wahrscheinlichkeitsrechnung", 5, 3, MATHEMATIK),
                // 4. Semester
                new Modul("Computing Plattform", 5, 4, INFORMATIK_GRUNDLAGEN),
                new Modul("Softwareengineering-Projekt", 10, 4, INFORMATIK_ANWENDUNGEN),
                new Modul("Usability-Engineering und Mensch-Maschine-Interaktion", 5, 4, INFORMATIK_ANWENDUNGEN),
                new Modul("Anwendungen", 5, 4, INFORMATIK_ANWENDUNGEN),
                new Modul("Agiles Projektmanagement", 5, 4, FACHUEBERGREIFEND),
                // 5. Semester
                new Modul("Security", 5, 5, INFORMATIK_GRUNDLAGEN),
                new Modul("Theoretische Informatik", 5, 5, INFORMATIK_GRUNDLAGEN),
                new Modul("Aktuelle Themen der Informatik", 5, 5, INFORMATIK_ANWENDUNGEN),
                new Modul("Vertiefungsprojekt", 10, 5, INFORMATIK_ANWENDUNGEN),
                new Modul("Künstliche Intelligenz", 5, 5, INFORMATIK_ANWENDUNGEN),
                // 6. Semester
                new Modul("Berufspraktische Tätigkeit", 30, 6, INFORMATIK_ANWENDUNGEN),
                // 7. Semester
                new Modul("Wahlmodul Internationalisierung", 5, 7, FACHUEBERGREIFEND),
                new Modul("Softskills", 5, 7, FACHUEBERGREIFEND),
                new Modul("Wissenschaftliches Arbeiten", 5, 7, FACHUEBERGREIFEND),
                new Modul("Bachelor-Thesis", 15, 7, INFORMATIK_ANWENDUNGEN)
        ));
    }
}