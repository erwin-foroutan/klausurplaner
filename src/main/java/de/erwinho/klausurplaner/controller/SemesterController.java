package de.erwinho.klausurplaner.controller;

import de.erwinho.klausurplaner.dto.ModulUebersicht;
import de.erwinho.klausurplaner.dto.SemesterUebersicht;
import de.erwinho.klausurplaner.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/semester")
@RequiredArgsConstructor
public class SemesterController {

    private final SemesterService semesterService;

    @GetMapping("/aktuell")
    public SemesterUebersicht aktuell(@AuthenticationPrincipal Jwt jwt) {
        return semesterService.aktuellesSemester(BenutzerId.aus(jwt));
    }

    @GetMapping("/verlauf")
    public List<ModulUebersicht> verlauf(@AuthenticationPrincipal Jwt jwt) {
        return semesterService.verlauf(BenutzerId.aus(jwt));
    }
}