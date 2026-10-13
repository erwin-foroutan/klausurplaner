package de.erwinho.klausurplaner.controller;

import de.erwinho.klausurplaner.dto.LoginRequest;
import de.erwinho.klausurplaner.dto.RegistrierungRequest;
import de.erwinho.klausurplaner.dto.TokenResponse;
import de.erwinho.klausurplaner.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registrieren")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse registrieren(@Valid @RequestBody RegistrierungRequest request) {
        return authService.registrieren(request.email(), request.passwort());
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.einloggen(request.email(), request.passwort());
    }
}