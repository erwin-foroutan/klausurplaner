package de.erwinho.klausurplaner.controller;

import de.erwinho.klausurplaner.dto.StudienprofilRequest;
import de.erwinho.klausurplaner.dto.StudienprofilResponse;
import de.erwinho.klausurplaner.entity.Studienprofil;
import de.erwinho.klausurplaner.service.StudienprofilService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studienprofil")
@RequiredArgsConstructor
public class StudienprofilController {

    private final StudienprofilService studienprofilService;

    @GetMapping
    public StudienprofilResponse laden() {
        Studienprofil profil = studienprofilService.laden();
        return new StudienprofilResponse(profil.getStartJahr(), studienprofilService.aktuellesSemester());
    }

    @PutMapping
    public StudienprofilResponse speichern(@RequestBody StudienprofilRequest request) {
        Studienprofil profil = studienprofilService.speichern(request.startJahr());
        return new StudienprofilResponse(profil.getStartJahr(), studienprofilService.aktuellesSemester());
    }
}