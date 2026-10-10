# Klausurplaner

REST-API zur Planung des Informatik-Studiums an der Hochschule RheinMain:
Studienverlauf, Prüfungsanmeldungen mit Versuchsregeln, Lernzeiten und Lernziele.

## Features

- *Studienplan* – alle 30 Module des B.Sc. Angewandte Informatik (PO 2024) werden beim Start automatisch angelegt
- *Aktuelles Semester* – wird aus dem Studienbeginn berechnet (Studienstart immer im Wintersemester)
- *Prüfungsanmeldungen mit echten Prüfungsregeln*
    - maximal 3 Versuche pro Modul
    - „nicht angetreten“ zählt nicht als Versuch
    - Warnung vor dem letzten Versuch, Sperre nach endgültigem Nichtbestehen
    - Note 1,0–4,0 bei bestanden, automatisch 5,0 bei nicht bestanden
- *Semesterübersicht* – Module des aktuellen Semesters plus alle offenen Module aus früheren Semestern, inkl. erreichter ECTS
- *Lerneinheiten und Lernziele* – Lernzeit pro Modul erfassen, Fortschritt gegenüber dem Ziel in Prozent
- *Validierung und Fehlerbehandlung* – verständliche Fehlermeldungen mit passenden HTTP-Statuscodes (400 / 409)

## Tech-Stack

- Java 25, Spring Boot (Web, Data JPA, Validation)
- PostgreSQL 17 in Docker
- Lombok
- JUnit 5
- Bruno (API-Tests, Collection im Ordner bruno/)

## Architektur


Controller  →  Service  →  Repository  →  PostgreSQL
(DTOs)     (Regeln)    (Spring Data)


- *Controller* nehmen Requests als DTOs (Java Records) entgegen und validieren sie
- *Services* enthalten die Geschäftslogik; die Regeln sind als reine Methoden umgesetzt und unabhängig von der Datenbank getestet
- *Berechnete Werte* (aktuelles Semester, Modulstatus, Lernfortschritt) werden nicht gespeichert, sondern bei Bedarf berechnet

## Starten

Voraussetzungen: Java 25, Docker

bash
# Datenbank starten
docker compose up -d

# Anwendung starten (Windows: mvnw.cmd)
./mvnw spring-boot:run


Die API läuft dann unter http://localhost:8080.

## Tests

bash
./mvnw test


## API-Überblick

| Methode | Pfad | Beschreibung |
|---|---|---|
| GET | /module | Alle Module des Studienplans |
| PUT | /studienprofil | Studienbeginn setzen |
| GET | /studienprofil | Studienbeginn und aktuelles Semester |
| GET | /semester/aktuell | Übersicht des aktuellen Semesters |
| POST | /pruefungen | Zur Prüfung anmelden |
| PUT | /pruefungen/{id}/ergebnis | Ergebnis eintragen |
| POST | /pruefungen/nachtragen | Vergangenes Ergebnis nachtragen |
| POST | /lerneinheiten | Lerneinheit erfassen |
| GET | /module/{id}/lerneinheiten | Lerneinheiten eines Moduls |
| POST | /lernziele | Lernziel anlegen |
| GET | /module/{id}/lernziele | Lernziele mit Fortschritt |

## Geplant

- Login mit Spring Security, damit jeder Nutzer eigene Daten hat
- Frontend mit React und TypeScript
- Docker-Image für die Anwendung und CI mit GitHub Actions