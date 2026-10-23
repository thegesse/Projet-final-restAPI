package com.goose.projetfinal.controller;

import com.goose.projetfinal.dto.AuteurRequest;
import com.goose.projetfinal.dto.AuteurResponse;
import com.goose.projetfinal.dto.LivreResponse;
import com.goose.projetfinal.mapper.AuteurMapper;
import com.goose.projetfinal.mapper.LivreMapper;
import com.goose.projetfinal.model.Auteur;
import com.goose.projetfinal.model.Livre;
import com.goose.projetfinal.service.AuteurService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auteurs")
public class AuteurController {

    private final AuteurService auteurService;

    public AuteurController(AuteurService auteurService) {
        this.auteurService = auteurService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<AuteurResponse>> listerTous(
            @PageableDefault(size = 10, sort = "nom") Pageable pageable) {
        Page<Auteur> page = auteurService.recupererTous(pageable);
        return ResponseEntity.ok(AuteurMapper.versPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuteurResponse> obtenirParId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(AuteurMapper.versResponse(auteurService.obtenirParId(id)));
        } catch (jakarta.persistence.EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AuteurResponse> creer(@Valid @RequestBody AuteurRequest request) {
        Auteur auteur = AuteurMapper.versEntite(request);
        Auteur auteurCree = auteurService.creer(auteur);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AuteurMapper.versResponse(auteurCree));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuteurResponse> mettreAJour(@PathVariable Long id,
                                                       @Valid @RequestBody AuteurRequest request) {
        try {
            Auteur nouvellesValeurs = AuteurMapper.versEntite(request);
            Auteur auteurModifie = auteurService.mettreAJour(id, nouvellesValeurs);
            return ResponseEntity.ok(AuteurMapper.versResponse(auteurModifie));
        } catch (jakarta.persistence.EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        try {
            auteurService.supprimer(id);
            return ResponseEntity.noContent().build();
        } catch (jakarta.persistence.EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (AuteurService.AuteurAvecLivresException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}/livres")
    public ResponseEntity<PagedModel<LivreResponse>> listerLivres(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "titre") Pageable pageable) {
        try {
            Page<Livre> page = auteurService.recupererLivres(id, pageable);
            return ResponseEntity.ok(LivreMapper.versPage(page));
        } catch (jakarta.persistence.EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
