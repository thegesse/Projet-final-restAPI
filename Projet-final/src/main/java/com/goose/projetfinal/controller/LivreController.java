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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Livres", description = "Catalogue des livres")
@RestController
@RequestMapping("/api/v1/livres")
public class LivreController {

    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

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

    @Operation(summary = "Créer un nouveau livre")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livre créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou auteur inexistant"),
            @ApiResponse(responseCode = "409", description = "ISBN déjà utilisé (RM2)")
    })
    @PostMapping
    public ResponseEntity<LivreResponse> creer(@Valid @RequestBody LivreRequest request) {
        Livre livre = LivreMapper.versEntite(request);
        Livre livreCree = livreService.creer(livre);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LivreMapper.versResponse(livreCree));
    }

    @Operation(summary = "Mettre à jour un livre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou auteur inexistant"),
            @ApiResponse(responseCode = "404", description = "Livre inexistant"),
            @ApiResponse(responseCode = "409", description = "ISBN déjà utilisé par un autre livre (RM2)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LivreResponse> mettreAJour(
            @PathVariable Long id,
            @Valid @RequestBody LivreRequest request) {
        Livre nouvellesValeurs = LivreMapper.versEntite(request);
        Livre livreModifie = livreService.mettreAJour(id, nouvellesValeurs);
        return ResponseEntity.ok(LivreMapper.versResponse(livreModifie));
    }

    @Operation(summary = "Supprimer un livre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livre supprimé"),
            @ApiResponse(responseCode = "404", description = "Livre inexistant")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        livreService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

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
        Livre livre = livreService.recupererParId(id);
        Livre livreModifie = livreService.ajouterStock(livre, quantite);
        return ResponseEntity.ok(LivreMapper.versResponse(livreModifie));
    }
}

