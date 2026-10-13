# Klausurplaner

[![CI](https://github.com/erwin-foroutan/klausurplaner/actions/workflows/ci.yml/badge.svg)](https://github.com/erwin-foroutan/klausurplaner/actions/workflows/ci.yml)

Full-Stack-Webanwendung zur Planung des Informatik-Studiums an der Hochschule RheinMain:
Studienverlauf, Prüfungsanmeldungen mit echten Versuchsregeln, Lernzeiten und Lernziele.

## Screenshots

**Dashboard** – aktuelles Semester, ECTS-Fortschritt, Prüfungsanmeldung und Ergebnisse per Klick

![Dashboard](docs/dashboard.png)

**Studienverlauf** – alle 7 Semester, Modulgröße entsprechend der Leistungspunkte

![Studienverlauf](docs/verlauf.png)

## Features

- **Studienplan** – alle 30 Module des B.Sc. Angewandte Informatik (PO 2024) werden beim Start automatisch angelegt
- **Aktuelles Semester** – wird aus dem gewählten Studienbeginn berechnet (Studienstart immer im Wintersemester)
- **Prüfungsanmeldungen mit echten Prüfungsregeln**
  - maximal 3 Versuche pro Modul
  - „nicht angetreten“ zählt nicht als Versuch
  - Warnung vor dem letzten Versuch, Sperre nach endgültigem Nichtbestehen
  - Note 1,0–4,0 bei bestanden, automatisch 5,0 bei nicht bestanden
- **Dashboard** – Module des aktuellen Semesters plus offene Module aus früheren Semestern, ECTS-Fortschritt
- **Studienverlauf** – Übersicht über alle Semester mit Status und Note pro Modul
- **Korrekturen** – letzten Eintrag eines Moduls rückgängig machen
- **Lerneinheiten und Lernziele** – Lernzeit pro Modul erfassen, Fortschritt gegenüber dem Ziel in Prozent (API)
- **Validierung und Fehlerbehandlung** – verständliche Fehlermeldungen mit passenden HTTP-Statuscodes (400 / 409), die im Frontend angezeigt werden

## Tech-Stack

**Backend**
- Java 25, Spring Boot (Web, Data JPA, Validation)
- PostgreSQL 17
- JUnit 5, Lombok

**Frontend**
- React mit TypeScript, Vite
- Tailwind CSS
- React Router

**Infrastruktur**
- Docker (Multi-Stage-Build) und Docker Compose
- GitHub Actions (CI: Tests bei jedem Push)
- Bruno (API-Tests, Collection im Ordner `bruno/`)

## Architektur

```
React (Vite)  →  REST-API (Spring Boot)  →  PostgreSQL
                 Controller → Service → Repository
```

- **Controller** nehmen Requests als DTOs (Java Records) entgegen und validieren sie
- **Services** enthalten die Geschäftslogik; die Prüfungsregeln sind als reine Methoden umgesetzt und unabhängig von der Datenbank getestet
- **Berechnete Werte** (aktuelles Semester, Modulstatus, Lernfortschritt) werden nicht gespeichert, sondern bei Bedarf berechnet
- Abfragen mit `JOIN FETCH` vermeiden das N+1-Problem

## Starten

### Backend mit Docker

Voraussetzung: Docker

```bash
docker compose --profile app up --build
```

Startet Datenbank und Backend. Die API läuft unter `http://localhost:8080`.

### Frontend

Voraussetzung: Node.js

```bash
cd frontend
npm install
npm run dev
```

Die Anwendung läuft dann unter `http://localhost:5173`.

### Backend für die Entwicklung

Voraussetzungen: Java 25, Docker

```bash
# Nur die Datenbank starten
docker compose up -d

# Backend starten (Windows: mvnw.cmd)
./mvnw spring-boot:run
```

## Tests

```bash
./mvnw test
```

Die Tests laufen außerdem automatisch bei jedem Push über GitHub Actions.

## API-Überblick

| Methode | Pfad | Beschreibung |
|---|---|---|
| GET | `/module` | Alle Module des Studienplans |
| PUT | `/studienprofil` | Studienbeginn setzen |
| GET | `/studienprofil` | Studienbeginn und aktuelles Semester |
| GET | `/semester/aktuell` | Übersicht des aktuellen Semesters |
| GET | `/semester/verlauf` | Alle Module mit Status |
| POST | `/pruefungen` | Zur Prüfung anmelden |
| PUT | `/pruefungen/{id}/ergebnis` | Ergebnis eintragen |
| POST | `/pruefungen/nachtragen` | Vergangenes Ergebnis nachtragen |
| DELETE | `/pruefungen/modul/{id}/letzte` | Letzten Eintrag eines Moduls rückgängig machen |
| POST | `/lerneinheiten` | Lerneinheit erfassen |
| GET | `/module/{id}/lerneinheiten` | Lerneinheiten eines Moduls |
| POST | `/lernziele` | Lernziel anlegen |
| GET | `/module/{id}/lernziele` | Lernziele mit Fortschritt |

## Geplant

- Lernzeiten und Lernziele im Frontend
- Login mit Spring Security, damit jeder Nutzer eigene Daten hat