package de.erwinho.klausurplaner.repository;

import de.erwinho.klausurplaner.entity.Lernziel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LernzielRepository extends JpaRepository<Lernziel, Long> {

    List<Lernziel> findByModulId(Long modulId);
}