package de.erwinho.klausurplaner.service;

import de.erwinho.klausurplaner.dto.TokenResponse;
import de.erwinho.klausurplaner.entity.Benutzer;
import de.erwinho.klausurplaner.repository.BenutzerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final BenutzerRepository benutzerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public TokenResponse registrieren(String email, String passwort) {
        String normalisiert = email.trim().toLowerCase();
        if (benutzerRepository.existsByEmail(normalisiert)) {
            throw new IllegalStateException("Diese E-Mail ist bereits registriert");
        }
        Benutzer benutzer = benutzerRepository.save(
                new Benutzer(normalisiert, passwordEncoder.encode(passwort)));
        return token(benutzer);
    }

    public TokenResponse einloggen(String email, String passwort) {
        Benutzer benutzer = benutzerRepository.findByEmail(email.trim().toLowerCase())
                .filter(b -> passwordEncoder.matches(passwort, b.getPasswortHash()))
                .orElseThrow(() -> new BadCredentialsException("E-Mail oder Passwort falsch"));
        return token(benutzer);
    }

    private TokenResponse token(Benutzer benutzer) {
        Instant jetzt = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("klausurplaner")
                .issuedAt(jetzt)
                .expiresAt(jetzt.plus(7, ChronoUnit.DAYS))
                .subject(String.valueOf(benutzer.getId()))
                .claim("email", benutzer.getEmail())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new TokenResponse(token, benutzer.getEmail());
    }
}