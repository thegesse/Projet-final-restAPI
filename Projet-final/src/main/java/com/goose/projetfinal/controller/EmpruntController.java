package com.goose.projetfinal.controller;

import com.goose.projetfinal.dto.EmpruntRequest;
import com.goose.projetfinal.dto.EmpruntResponse;
import com.goose.projetfinal.mapper.EmpruntMapper;
import com.goose.projetfinal.model.Emprunt;
import com.goose.projetfinal.service.EmpruntService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Emprunts", description = "Emprunts et retours des livres")
@RestController
@RequestMapping("/api/v1/emprunts")
public class EmpruntController {

    private final EmpruntService empruntService;

    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }

    // Endpoint 15 — GET /api/v1/emprunts
    @Operation(summary = "Lister tous les emprunts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page des emprunts récupérée")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EmpruntResponse>> listerTous(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererTous(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    // Endpoint 16 — GET /api/v1/emprunts/{id}
    @Operation(summary = "Obtenir un emprunt par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emprunt trouvé"),
            @ApiResponse(responseCode = "404", description = "Emprunt inexistant")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmpruntResponse> obtenirParId(@PathVariable Long id) {
        Emprunt emprunt = empruntService.obtenirParId(id);
        return ResponseEntity.ok(EmpruntMapper.versResponse(emprunt));
    }

    @PostMapping
    public ResponseEntity<EmpruntResponse> creer(@Valid @RequestBody EmpruntRequest request) {
        Emprunt emprunt = EmpruntMapper.versEntite(request);
        Emprunt empruntCree = empruntService.creer(emprunt);
        return ResponseEntity.status(HttpStatus.CREATED).body(EmpruntMapper.versResponse(empruntCree));
    }

    // Endpoint 18 — PATCH /api/v1/emprunts/{id}/retour
    @Operation(summary = "Retourner un livre emprunté (RM6)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emprunt retourné avec succès"),
            @ApiResponse(responseCode = "404", description = "Emprunt inexistant"),
            @ApiResponse(responseCode = "409", description = "Emprunt déjà rendu (RM6)")
    })
    @PatchMapping("/{id}/retour")
    public ResponseEntity<EmpruntResponse> retourner(@PathVariable Long id) {
        Optional<Emprunt> opt = empruntService.recupererParId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Emprunt emprunt = opt.get();
        if (Boolean.TRUE.equals(emprunt.getRendu())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Emprunt maj = empruntService.retourner(emprunt);
        return ResponseEntity.ok(EmpruntMapper.versResponse(maj));
    }

    // Endpoint 19 — GET /api/v1/emprunts/en-cours
    @Operation(summary = "Lister les emprunts en cours (non rendus)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page des emprunts en cours")
    })
    @GetMapping("/en-cours")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnCours(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnCours(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    // Endpoint 20 — GET /api/v1/emprunts/en-retard
    @Operation(summary = "Lister les emprunts en retard (RM7)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page des emprunts en retard")
    })
    public ResponseEntity<EmpruntResponse> retour(@PathVariable Long id) {
        Emprunt empruntRendu = empruntService.retourner(id);
        return ResponseEntity.ok(EmpruntMapper.versResponse(empruntRendu));
    }

    @GetMapping("/en-cours")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnCours(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnCours(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

    @GetMapping("/en-retard")
    public ResponseEntity<PagedModel<EmpruntResponse>> listerEnRetard(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererEnRetard(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }
}
