package com.goose.projetfinal.controller;

import com.goose.projetfinal.dto.AuteurRequest;
import com.goose.projetfinal.dto.AuteurResponse;
import com.goose.projetfinal.dto.LivreResponse;
import com.goose.projetfinal.mapper.AuteurMapper;
import com.goose.projetfinal.mapper.LivreMapper;
import com.goose.projetfinal.model.Auteur;
import com.goose.projetfinal.service.AuteurService;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auteurs")
public class AuteurController {

    private final AuteurService auteurService;
    private final AuteurMapper auteurMapper;

    public AuteurController(AuteurService auteurService, AuteurMapper auteurMapper) {
        this.auteurService = auteurService;
        this.auteurMapper = auteurMapper;
    }

    // Écrivez ici les méthodes des endpoints 1 à 5
    @GetMapping
    public ResponseEntity<List<AuteurResponse>> listerTous() {
        List<AuteurResponse> auteur = auteurService.recupererTous();
        return ResponseEntity.ok(auteur);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> obtenirParId(@PathVariable Long id) {
        return auteurService.recupererParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/livres")
    public ResponseEntity<PagedModel<LivreResponse>> listerLivres(@PathVariable Long id,  @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<LivreResponse> livres = auteurService.recupererParLivreId(id, pageable);
        return ResponseEntity.ok(new PagedModel<>(livres));

    }

    @PostMapping
    public ResponseEntity<AuteurResponse> creer(@Valid @RequestBody AuteurRequest request) {
        AuteurResponse response = auteurService.creer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuteurResponse> mettreAJour(@PathVariable Long id,
            @Valid @RequestBody AuteurRequest request) {
        return auteurService.mettreAJour(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (auteurService.supprimer(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
