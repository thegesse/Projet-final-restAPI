package com.goose.projetfinal.controller;

import com.goose.projetfinal.dto.EmpruntRequest;
import com.goose.projetfinal.dto.EmpruntResponse;
import com.goose.projetfinal.mapper.EmpruntMapper;
import com.goose.projetfinal.model.Emprunt;
import com.goose.projetfinal.service.EmpruntService;
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

@RestController
@RequestMapping("/api/v1/emprunts")
public class EmpruntController {

    private final EmpruntService empruntService;

    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EmpruntResponse>> listerTous(
            @PageableDefault(size = 10, sort = "dateEmprunt") Pageable pageable) {
        Page<Emprunt> page = empruntService.recupererTous(pageable);
        return ResponseEntity.ok(EmpruntMapper.versPage(page));
    }

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

    @PatchMapping("/{id}/retour")
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
