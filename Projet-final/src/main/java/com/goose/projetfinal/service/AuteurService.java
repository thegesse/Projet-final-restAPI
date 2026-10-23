package com.goose.projetfinal.service;

import com.goose.projetfinal.model.Auteur;
import com.goose.projetfinal.model.Livre;
import com.goose.projetfinal.repository.AuteurRepository;
import com.goose.projetfinal.repository.LivreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuteurService {

    private final AuteurRepository auteurRepository;
    private final LivreRepository livreRepository;

    public AuteurService(AuteurRepository auteurRepository, LivreRepository livreRepository) {
        this.auteurRepository = auteurRepository;
        this.livreRepository = livreRepository;
    }

    public Page<Auteur> recupererTous(Pageable pageable) {
        return auteurRepository.findAll(pageable);
    }

    public Optional<Auteur> recupererParId(Long id) {
        return auteurRepository.findById(id);
    }

    public Auteur obtenirParId(Long id) {
        return auteurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auteur non trouvé"));
    }

    public boolean existe(Long id) {
        return auteurRepository.existsById(id);
    }

    public Auteur creer(Auteur auteur) {
        auteur.setId(null);
        return auteurRepository.save(auteur);
    }

    public Auteur mettreAJour(Long id, Auteur nouvellesValeurs) {
        Auteur auteurExistant = obtenirParId(id);
        auteurExistant.setNom(nouvellesValeurs.getNom());
        auteurExistant.setPrenom(nouvellesValeurs.getPrenom());
        auteurExistant.setNationalite(nouvellesValeurs.getNationalite());
        return auteurRepository.save(auteurExistant);
    }

    public boolean aDesLivres(Long id) {
        return livreRepository.existsByAuteurId(id);
    }

    public void supprimer(Long id) {
        if (!auteurRepository.existsById(id)) {
            throw new EntityNotFoundException("Auteur non trouvé");
        }
        if (aDesLivres(id)) {
            throw new AuteurAvecLivresException();
        }
        auteurRepository.deleteById(id);
    }

    public Page<Livre> recupererLivres(Long id, Pageable pageable) {
        if (!auteurRepository.existsById(id)) {
            throw new EntityNotFoundException("Auteur non trouvé");
        }
        return livreRepository.findByAuteurId(id, pageable);
    }

    public static class AuteurAvecLivresException extends RuntimeException {
        public AuteurAvecLivresException() {
            super("L'auteur a encore des livres");
        }
    }
}
