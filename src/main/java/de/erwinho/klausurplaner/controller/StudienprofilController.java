package de.erwinho.klausurplaner.controller;

import de.erwinho.klausurplaner.dto.StudienprofilRequest;
import de.erwinho.klausurplaner.dto.StudienprofilResponse;
import de.erwinho.klausurplaner.entity.Studienprofil;
import de.erwinho.klausurplaner.service.StudienprofilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studienprofil")
@RequiredArgsConstructor
public class StudienprofilController {

    private final StudienprofilService studienprofilService;

    @GetMapping
    public StudienprofilResponse laden(@AuthenticationPrincipal Jwt jwt) {
        Long benutzerId = BenutzerId.aus(jwt);
        Studienprofil profil = studienprofilService.laden(benutzerId);
        return new StudienprofilResponse(profil.getStartJahr(), studienprofilService.aktuellesSemester(benutzerId));
    }

    @PutMapping
    public StudienprofilResponse speichern(@AuthenticationPrincipal Jwt jwt,
                                           @Valid @RequestBody StudienprofilRequest request) {
        Long benutzerId = BenutzerId.aus(jwt);
        Studienprofil profil = studienprofilService.speichern(benutzerId, request.startJahr());
        return new StudienprofilResponse(profil.getStartJahr(), studienprofilService.aktuellesSemester(benutzerId));
    }
}