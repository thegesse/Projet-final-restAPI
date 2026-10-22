package com.goose.projetfinal.controller;

import com.goose.projetfinal.service.AuteurService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auteurs")
public class AuteurController {

    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }

    // Écrivez ici les méthodes des endpoints 1 à 5
}
