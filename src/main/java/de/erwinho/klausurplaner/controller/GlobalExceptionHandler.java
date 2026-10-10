package de.erwinho.klausurplaner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> ungueltigeEingabe(IllegalArgumentException e) {
        return Map.of("fehler", e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> konflikt(IllegalStateException e) {
        return Map.of("fehler", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validierungsfehler(MethodArgumentNotValidException e) {
        Map<String, String> fehler = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(f -> fehler.put(f.getField(), f.getDefaultMessage()));
        return fehler;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> unlesbaresJson(HttpMessageNotReadableException e) {
        return Map.of("fehler", "Ungültiges JSON oder ungültiger Wert (z. B. falscher Status oder falsches Datumsformat)");
    }
}