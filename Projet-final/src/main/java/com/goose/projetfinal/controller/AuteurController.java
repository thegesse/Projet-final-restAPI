package com.goose.projetfinal.controller;

import com.goose.projetfinal.service.AuteurService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auteurs")
public class AuteurController {

    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }

    // Écrivez ici les méthodes des endpoints 1 à 5
    @GetMapping 
    public ResponseEntity<List<AuteurResponse>> listerTous() {
        List<AuteurResponse> auteur = auteurService.recupererTous()
            .stream()
            .map(auteurMapper::versResponse)
            .toList
        return ResponseEntity.ok(auteur);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> obtenirParId(@PathVariable Long id) {
        return auteurService.recupererParId(id)
                .map(auteurMapper::versResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/livres")
    public ResponseEntity<List<LivreResponse>> listerLivres(@PathVariable Long id) {
        return auteurService.recupererParLivreId(id)
                .map(LivreMapper::versResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<AuteurResponse> creer(@Valid @RequestBody AuteurRequest request) {
        Auteur entiteACreer = auteurMapper.versEntite(request);
        Auteur entiteCree = auteurService.creer(entiteACreer);
        AuteurResponse response = auteurMapper.versResponse(entiteCree);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuteurResponse> mettreAJour(@PathVariable Long id,
            @Valid @RequestBody AuteurRequest request) {
        Auteur entiteModifiee = auteurMapper.versEntite(request);

        return auteurService.mettreAJour(id, entiteModifiee)
                .map(auteurMapper::versResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        auteurService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
