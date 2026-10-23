package com.goose.projetfinal.controller;

import com.goose.projetfinal.service.EmpruntService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<EmpruntResponse> obtenirParId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(empruntService.recupererParId(id);
        } catch (EntityNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/en-cours")
    public ResponseEntity<List<EmpruntResponse>> listerEnCours() {
        List<EmpruntResponse> emprunt = empruntService.recupererEmpruntsEnCours();
        return ResponseEntity.ok(emprunt);
    }

}
