package com.goose.projetfinal.service;

import com.goose.projetfinal.repository.AuteurRepository;
import com.goose.projetfinal.repository.LivreRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuteurService {

    private final AuteurRepository auteurRepository;
    private final LivreRepository livreRepository;

    public AuteurService(AuteurRepository auteurRepository, LivreRepository livreRepository) {
        this.auteurRepository = auteurRepository;
        this.livreRepository = livreRepository;
    }

    // Écrivez ici les méthodes du tableau
    public List<Auteur> recupererTous() {
        return auteurRepository.findAll();
    }

    public Optional<Auteur> recupererParId(Long id) {
        return auteurRepository.findById(id);
    }

    public Optional<List<Auteur>> recupererParLivreId(Long id) {
        return auteurRepository.findByLivresId(id);
    }

    public Auteur creer(Auteur auteur) {
        return auteurRepository.save(auteur);
    }

    public Auteur mettreAJour(Long id, Auteur auteur) {
        return auteurRepository.findById(id)
                .map(auteurExistant -> {
                    auteurExistant.setNom(auteur.getNom());
                    auteurExistant.setPrenom(auteur.getPrenom());
                    auteurExistant.setNationalite(auteur.getNationalite());
                    return auteurRepository.save(auteurExistant);
                })
                .orElseThrow(() -> new EntityNotFoundException("Auteur non trouvé"));
    }

    public boolean supprimer(Long id) {
        if (auteurRepository.existsById(id)) {
            auteurRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
