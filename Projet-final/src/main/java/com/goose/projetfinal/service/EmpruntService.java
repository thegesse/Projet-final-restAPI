package com.goose.projetfinal.service;

import com.goose.projetfinal.repository.EmpruntRepository;
import com.goose.projetfinal.repository.LivreRepository;

import java.util.stream.Collectors;

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
    public List<EmpruntResponse> recupererTous() {
        return empruntRepository.findAll()
                .stream()
                .map(EmpruntMapper::versReponse).toList();
    }

    public List<EmpruntResponse> enRetard() {
        return empruntRepository.findEnRetard()
                .stream()
                .map(EmpruntMapper::versReponse)
                .toList();
    }

    public List<EmpruntResponse> enCours() {
        return empruntRepository.findEnCours()
                .stream()
                .map(EmpruntMapper::versReponse)
                .toList();
    }

    public EmpruntResponse recupererParId(long id) {
        return empruntRepository.findById(id)
                .map(EmpruntMapper::versReponse)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));
    }

    public EmpruntResponse retourner(long id) {
        Emprunt emprunt = empruntRepository.findById(id).orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));
        emprunt.setDisponible(true);
        return EmpruntMapper.versReponse(empruntRepository.save(emprunt));
    }

    public EmpruntResponse enCoursParLivreId(long livreId) {
        return empruntRepository.findEnCoursByLivreId(livreId)
                .map(EmpruntMapper::versReponse)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));
    }

    public EmpruntResponse enRetardParLivreId(long livreId) {
        return empruntRepository.findEnRetardByLivreId(livreId)
                .map(EmpruntMapper::versReponse)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));
    }

    public EmpruntResponse creer(EmpruntRequest request) {
        Emprunt entiteACreer = EmpruntMapper.versEntite(request);
        Emprunt entiteCree = empruntRepository.save(entiteACreer);
        return EmpruntMapper.versReponse(entiteCree);
    }

    public EmpruntResponse miseAJour(long id, EmpruntRequest request) {
        return EmpruntRepository.findById(id)
            .map(empruntExistant -> {
                empruntExistant.setLivreId(request.getLivreId());
                empruntExistant.setEmprunteur(request.getEmprunteur());
                empruntExistant.setEmailEmprunteur(request.getEmailEmprunteur());
                empruntExistant.setDateEmprunt(request.getDateEmprunt());
                empruntExistant.setDateRetourPrevue(request.getDateRetourPrevue());
                empruntExistant.setDateRetourEffective(request.getDateRetourEffective());
                empruntExistant.setRendu(request.getRendu());

                Emprunt empruntModifie = empruntRepository.save(empruntExistant);
                return EmpruntMapper.versReponse(empruntModifie);
            })
            .orElseThrow(() -> new RuntimeException("Emprunt non trouvé"));

    public void supprimer(long id) {
        if (!empruntRepository.existsById(id)) {
            throw new RuntimeException("Emprunt non trouvé");
        }
        empruntRepository.deleteById(id);
    }

}
