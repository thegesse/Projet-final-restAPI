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
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Optional<Livre> resultat = livreService.recupererParId(id);
        if (resultat.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(LivreMapper.versResponse(resultat.get()));
    }

    // Endpoint 9 — POST /api/v1/livres
    @Operation(summary = "Créer un nouveau livre")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livre créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou auteur inexistant"),
            @ApiResponse(responseCode = "409", description = "ISBN déjà utilisé (RM2)")
    })
    @PostMapping
    public ResponseEntity<LivreResponse> creer(@Valid @RequestBody LivreRequest request) {
        if (!livreService.auteurExiste(request.getAuteurId())) {
            return ResponseEntity.badRequest().build();
        }
        if (livreService.isbnExiste(request.getIsbn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Livre entite = LivreMapper.versEntite(request);
        Livre livreCree = livreService.creer(entite);
        return ResponseEntity.status(HttpStatus.CREATED).body(LivreMapper.versResponse(livreCree));
    }

    // Endpoint 10 — PUT /api/v1/livres/{id}
    @Operation(summary = "Mettre à jour un livre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou auteur inexistant"),
            @ApiResponse(responseCode = "404", description = "Livre inexistant"),
            @ApiResponse(responseCode = "409", description = "ISBN déjà utilisé par un autre livre (RM2)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LivreResponse> mettreAJour(@PathVariable Long id, @Valid @RequestBody LivreRequest request) {
        Optional<Livre> opt = livreService.recupererParId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!livreService.auteurExiste(request.getAuteurId())) {
            return ResponseEntity.badRequest().build();
        }
        if (livreService.isbnUtiliseParUnAutreLivre(request.getIsbn(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Livre nouvellesValeurs = LivreMapper.versEntite(request);
        Livre maj = livreService.mettreAJour(opt.get(), nouvellesValeurs);
        return ResponseEntity.ok(LivreMapper.versResponse(maj));
    }

    // Endpoint 11 — DELETE /api/v1/livres/{id}
    @Operation(summary = "Supprimer un livre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livre supprimé"),
            @ApiResponse(responseCode = "404", description = "Livre inexistant")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!livreService.existe(id)) {
            return ResponseEntity.notFound().build();
        }
        livreService.supprimer(id);
        return ResponseEntity.noContent().build();
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
    }

    // Endpoint 14 — PATCH /api/v1/livres/{id}/stock?quantite=...
    @Operation(summary = "Ajouter du stock à un livre (réapprovisionnement)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock mis à jour"),
            @ApiResponse(responseCode = "400", description = "Paramètre quantite absent, non numérique ou inférieur ou égal à 0"),
            @ApiResponse(responseCode = "404", description = "Livre inexistant")
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<LivreResponse> ajouterStock(
            @PathVariable Long id,
            @RequestParam Integer quantite) {
        if (quantite <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Livre> opt = livreService.recupererParId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Livre maj = livreService.ajouterStock(opt.get(), quantite);
        return ResponseEntity.ok(LivreMapper.versResponse(maj));
    }
}
