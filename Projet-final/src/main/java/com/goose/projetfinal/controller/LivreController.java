
package com.goose.projetfinal.controller;

import com.goose.projetfinal.service.LivreService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/v1/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    // Écrivez ici les méthodes des endpoints 7 à 14
    @GetMapping
    public ResponseEntity<List<LivreResponse>> listerTous() {
        List<LivreResponse> livre = livreService.recupererTous()
                .stream()
                .map(livreMapper::versResponse)
                .toList();

        return ResponseEntity.ok(livre);
    }

}
