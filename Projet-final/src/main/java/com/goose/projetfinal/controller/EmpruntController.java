package com.goose.projetfinal.controller;

import com.goose.projetfinal.service.EmpruntService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emprunts")
public class EmpruntController {

    private final EmpruntService empruntService;

    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }

    // Écrivez ici les méthodes des endpoints 15 à 20
    @GetMapping
    public ResponsEntity<List<EmpruntResponse>> listerTous() {
        List<EmpruntResponse> emprunt = empruntService.recupererTous();
        return ResponseEntity.ok(emprunt);
    }

}
