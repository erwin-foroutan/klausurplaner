package de.erwinho.klausurplaner.repository;

import de.erwinho.klausurplaner.entity.Benutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenutzerRepository extends JpaRepository<Benutzer, Long> {

    Optional<Benutzer> findByEmail(String email);

    boolean existsByEmail(String email);
}