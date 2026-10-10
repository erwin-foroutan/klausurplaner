package de.erwinho.klausurplaner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lernziel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Modul modul;

    @Column(nullable = false)
    private int zielStunden;

    @Column(nullable = false)
    private LocalDate startDatum;

    @Column(nullable = false)
    private LocalDate deadline;

    private String beschreibung;
}