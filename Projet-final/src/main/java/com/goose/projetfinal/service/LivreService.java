package com.goose.projetfinal.service;

import com.goose.projetfinal.repository.AuteurRepository;
import com.goose.projetfinal.repository.LivreRepository;
import org.springframework.stereotype.Service;

@Service
public class LivreService {

    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;

    public LivreService(LivreRepository livreRepository, AuteurRepository auteurRepository) {
        this.livreRepository = livreRepository;
        this.auteurRepository = auteurRepository;
    }

    // Écrivez ici les méthodes du tableau
}
