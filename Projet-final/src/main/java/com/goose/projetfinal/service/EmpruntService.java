package com.goose.projetfinal.service;

import com.goose.projetfinal.model.Emprunt;
import com.goose.projetfinal.model.Livre;
import com.goose.projetfinal.repository.EmpruntRepository;
import com.goose.projetfinal.repository.LivreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmpruntService {

    /** Durée maximale d'un emprunt, en jours (RM5). */
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

    public boolean livreExiste(Long livreId) {
        return livreRepository.existsById(livreId);
    }

    public boolean livreDisponible(Long livreId) {
        Optional<Livre> livre = livreRepository.findById(livreId);
        return livre.isPresent() && livre.get().getStock() > 0;
    }

    public Emprunt creer(Emprunt emprunt) {
        emprunt.setId(null);
        emprunt.setDateEmprunt(LocalDate.now());
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(DUREE_MAX_JOURS));
        emprunt.setDateRetourEffective(null);
        emprunt.setRendu(false);

        Optional<Livre> livreOpt = livreRepository.findById(emprunt.getLivreId());
        if (livreOpt.isPresent()) {
            Livre livre = livreOpt.get();
            livre.setStock(livre.getStock() - 1);
            livreRepository.save(livre);
        }

        return empruntRepository.save(emprunt);
    }

    public Emprunt retourner(Emprunt emprunt) {
        emprunt.setRendu(true);
        emprunt.setDateRetourEffective(LocalDate.now());

        Optional<Livre> livreOpt = livreRepository.findById(emprunt.getLivreId());
        if (livreOpt.isPresent()) {
            Livre livre = livreOpt.get();
            livre.setStock(livre.getStock() + 1);
            livreRepository.save(livre);
        }

        return empruntRepository.save(emprunt);
    }

    public Page<Emprunt> recupererEnCours(Pageable pageable) {
        return empruntRepository.findByRenduFalse(pageable);
    }

    public Page<Emprunt> recupererEnRetard(Pageable pageable) {
        return empruntRepository.findByRenduFalseAndDateRetourPrevueBefore(LocalDate.now(), pageable);
    }
}
