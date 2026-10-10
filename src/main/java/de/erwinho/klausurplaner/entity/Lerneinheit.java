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
public class Lerneinheit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Modul modul;

    @Column(nullable = false)
    private LocalDate datum;

    @Column(nullable = false)
    private int dauerMinuten;

    private String thema;

    private String notiz;
}