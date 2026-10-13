package de.erwinho.klausurplaner.repository;

import de.erwinho.klausurplaner.entity.Studienprofil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudienprofilRepository extends JpaRepository<Studienprofil, Long> {

    Optional<Studienprofil> findByBenutzerId(Long benutzerId);
}