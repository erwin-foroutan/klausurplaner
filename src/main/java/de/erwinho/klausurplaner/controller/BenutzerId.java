package de.erwinho.klausurplaner.controller;

import org.springframework.security.oauth2.jwt.Jwt;

final class BenutzerId {

    private BenutzerId() {
    }

    static Long aus(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}