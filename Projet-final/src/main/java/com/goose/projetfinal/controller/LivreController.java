package com.goose.projetfinal.controller;

import com.goose.projetfinal.dto.LivreRequest;
import com.goose.projetfinal.dto.LivreResponse;
import com.goose.projetfinal.mapper.LivreMapper;
import com.goose.projetfinal.model.Livre;
import com.goose.projetfinal.service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

import java.util.Optional;

@Tag(name = "Livres", description = "Catalogue des livres")
@RestController
@RequestMapping("/api/v1/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    // Endpoint 7 — GET /api/v1/livres
    @Operation(summary = "Lister tous les livres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page des livres récupérée")
    })
    @GetMapping
    public ResponseEntity<PagedModel<LivreResponse>> listerTous(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.recupererTous(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    // Endpoint 8 — GET /api/v1/livres/{id}
    @Operation(summary = "Obtenir un livre par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre trouvé"),
            @ApiResponse(responseCode = "404", description = "Livre inexistant")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LivreResponse> obtenirParId(@PathVariable Long id) {
        Livre livre = livreService.recupererParId(id);
        return ResponseEntity.ok(LivreMapper.versResponse(livre));
    }

    // Endpoint 12 — GET /api/v1/livres/recherche?titre=...
    @Operation(summary = "Rechercher des livres par titre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Résultats de la recherche paginés"),
            @ApiResponse(responseCode = "400", description = "Paramètre titre absent")
    })
    @GetMapping("/recherche")
    public ResponseEntity<PagedModel<LivreResponse>> rechercher(
            @RequestParam String titre,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.rechercherParTitre(titre, pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    // Endpoint 13 — GET /api/v1/livres/disponibles
    @Operation(summary = "Lister les livres disponibles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page des livres disponibles en stock (RM4)")
    })
    @GetMapping("/disponibles")
    public ResponseEntity<PagedModel<LivreResponse>> listerDisponibles(
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        Page<Livre> page = livreService.recupererDisponibles(pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
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

    // Endpoint 14 — PATCH /api/v1/livres/{id}/stock?quantite=...
    @Operation(summary = "Ajouter du stock à un livre (réapprovisionnement)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock mis à jour"),
            @ApiResponse(responseCode = "400", description = "Paramètre quantite absent, non numérique ou inférieur ou égal à 0"),
            @ApiResponse(responseCode = "404", description = "Livre inexistant")
    })
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
