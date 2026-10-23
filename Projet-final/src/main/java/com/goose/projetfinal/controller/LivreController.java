
package com.goose.projetfinal.controller;

import com.goose.projetfinal.dto.LivreRequest;
import com.goose.projetfinal.dto.LivreResponse;
import com.goose.projetfinal.mapper.LivreMapper;
import com.goose.projetfinal.model.Livre;
import com.goose.projetfinal.service.LivreService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<PagedModel<LivreResponse>> listerTous(@PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> livre = livreService.recupererTous(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(livre));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreResponse> obtenirParId(@PathVariable Long id) {
        Livre livre = livreService.recupererParId(id);
        return ResponseEntity.ok(LivreMapper.versResponse(livre));
    }

    @GetMapping("/recherche")
    public ResponseEntity<PagedModel<LivreResponse>> rechercher(
            @RequestParam String titre,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {

        Page<Livre> page = livreService.rechercherParTitre(titre, pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<PagedModel<LivreResponse>> listerDisponibles(@PageableDefault(size = 10, sort = "titre")Pageable pageable) {
        Page<Livre> page = livreService.recupererDisponibles(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    @PostMapping
    public ResponseEntity<LivreResponse> creer(@Valid @RequestBody LivreRequest request) {
        Livre livre = LivreMapper.versEntite(request);
        Livre livreCree = livreService.creer(livre);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LivreMapper.versResponse(livreCree));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<LivreResponse> ajouterStock(@PathVariable Long id, @RequestParam Integer quantite) {
        Livre livre = livreService.recupererParId(id);
        Livre livreModifie = livreService.ajouterStock(livre, quantite);
        return ResponseEntity.ok(LivreMapper.versResponse(livreModifie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivreResponse> mettreAJour(@PathVariable Long id, @Valid @RequestBody LivreRequest request) {
        Livre livre = LivreMapper.versEntite(request);
        Livre livreModifie = livreService.mettreAJour(id, livre);
        return ResponseEntity.ok(LivreMapper.versResponse(livreModifie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        livreService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

}
