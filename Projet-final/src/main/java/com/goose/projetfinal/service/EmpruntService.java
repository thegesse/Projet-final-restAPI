package com.goose.projetfinal.service;

import com.goose.projetfinal.repository.EmpruntRepository;
import com.goose.projetfinal.repository.LivreRepository;
import org.springframework.stereotype.Service;

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

    // Écrivez ici les méthodes du tableau
}
