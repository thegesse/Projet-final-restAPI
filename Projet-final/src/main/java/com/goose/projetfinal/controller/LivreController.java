package com.goose.projetfinal.controller;

import com.goose.projetfinal.service.LivreService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    // Écrivez ici les méthodes des endpoints 7 à 14
}
