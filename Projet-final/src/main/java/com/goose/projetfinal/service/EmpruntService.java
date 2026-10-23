package com.goose.projetfinal.service;

import com.goose.projetfinal.model.Emprunt;
import com.goose.projetfinal.model.Livre;
import com.goose.projetfinal.repository.EmpruntRepository;
import com.goose.projetfinal.repository.LivreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmpruntService {

    public static final int DUREE_MAX_JOURS = 14;

    private final EmpruntRepository empruntRepository;
    private final LivreRepository livreRepository;

    public EmpruntService(EmpruntRepository empruntRepository, LivreRepository livreRepository) {
        this.empruntRepository = empruntRepository;
        this.livreRepository = livreRepository;
    }

    public Page<Emprunt> recupererTous(Pageable pageable) {
        return empruntRepository.findAll(pageable);
    }

    public Optional<Emprunt> recupererParId(Long id) {
        return empruntRepository.findById(id);
    }

    public Emprunt obtenirParId(Long id) {
        return empruntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprunt inexistant"));
    }

    public boolean livreExiste(Long livreId) {
        return livreRepository.existsById(livreId);
    }

    public boolean livreDisponible(Long livreId) {
        return livreRepository.findById(livreId)
                .map(livre -> livre.getStock() != null && livre.getStock() > 0)
                .orElse(false);
    }


    @Transactional
    public Emprunt creer(Emprunt emprunt) {
        Long livreId = emprunt.getLivreId();
        if (!livreExiste(livreId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le livre n'existe pas");
        }
        if (!livreDisponible(livreId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Le livre n'est pas disponible");
        }

        Livre livre = livreRepository.findById(livreId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le livre n'existe pas"));
        LocalDate aujourdHui = LocalDate.now();
        emprunt.setId(null);
        emprunt.setDateEmprunt(aujourdHui);
        emprunt.setDateRetourPrevue(aujourdHui.plusDays(DUREE_MAX_JOURS));
        emprunt.setDateRetourEffective(null);
        emprunt.setRendu(false);
        livre.setStock(livre.getStock() - 1);
        livreRepository.save(livre);
        return empruntRepository.save(emprunt);
    }

    @Transactional
    public Emprunt retourner(Long id) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprunt inexistant"));
        if (Boolean.TRUE.equals(emprunt.getRendu())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "L'emprunt est déjà rendu");
        }
        emprunt.setRendu(true);
        emprunt.setDateRetourEffective(LocalDate.now());

        livreRepository.findById(emprunt.getLivreId()).ifPresent(livre -> {
            livre.setStock(livre.getStock() + 1);
            livreRepository.save(livre);
        });
        return empruntRepository.save(emprunt);
    }

    public Page<Emprunt> recupererEnCours(Pageable pageable) {
        return empruntRepository.findByRenduFalse(pageable);
    }

    public Page<Emprunt> recupererEnRetard(Pageable pageable) {
        return empruntRepository.findByRenduFalseAndDateRetourPrevueBefore(LocalDate.now(), pageable);
    }
}
