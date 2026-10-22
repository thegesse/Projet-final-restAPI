
package com.goose.projetfinal.controller;

import com.goose.projetfinal.service.LivreService;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
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

    @GetMapping("/{id}")
    public ResponseEntity<LivreResponse> obtenirParId(@PathVariable Long id) {
        return livreService.recupererParId(id)
                .map(livreMapper::versResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recherche")
    public ResponseEntity<PagedModel<LivreResponse>> rechercher(
            @RequestParam String titre,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {

        Page<Livre> page = livreService.rechercherParTitre(titre, pageable);
        return ResponseEntity.ok(LivreMapper.versPage(page));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LivreResponse>> listerDisponibles() {
        List<LivreResponse> livre = livreService.recupererLivreDisponible()
                .stream()
                .map(livreMapper::versResponse)
                .toList();

        return ResponseEntity.ok(livre);
    }
}
