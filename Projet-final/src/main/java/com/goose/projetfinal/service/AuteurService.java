package com.goose.projetfinal.service;

import com.goose.projetfinal.repository.AuteurRepository;
import com.goose.projetfinal.repository.LivreRepository;
import org.springframework.stereotype.Service;

@Service
public class AuteurService {

    private final AuteurRepository auteurRepository;
    private final LivreRepository livreRepository;

    public AuteurService(AuteurRepository auteurRepository, LivreRepository livreRepository) {
        this.auteurRepository = auteurRepository;
        this.livreRepository = livreRepository;
    }

    // Écrivez ici les méthodes du tableau
}
