package com.goose.projetfinal.service;

import com.goose.projetfinal.dto.AuteurRequest;
import com.goose.projetfinal.dto.AuteurResponse;
import com.goose.projetfinal.dto.LivreResponse;
import com.goose.projetfinal.mapper.AuteurMapper;
import com.goose.projetfinal.mapper.LivreMapper;
import com.goose.projetfinal.model.Auteur;
import com.goose.projetfinal.repository.AuteurRepository;
import com.goose.projetfinal.repository.LivreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuteurService {

    private final AuteurRepository auteurRepository;
    private final LivreRepository livreRepository;
    private final AuteurMapper auteurMapper;

    public AuteurService(AuteurRepository auteurRepository, LivreRepository livreRepository, AuteurMapper auteurMapper) {
        this.auteurRepository = auteurRepository;
        this.livreRepository = livreRepository;
        this.auteurMapper = auteurMapper;
    }

    // Écrivez ici les méthodes du tableau
    public List<AuteurResponse> recupererTous() {
        return auteurRepository.findAll()
                .stream()
                .map(AuteurMapper::versResponse).toList();
    }

    public Optional<AuteurResponse> recupererParId(Long id) {
        return auteurRepository.findById(id)
                .map(AuteurMapper::versResponse);
    }

    public Page<LivreResponse> recupererParLivreId(Long id, Pageable pageable) {
        return livreRepository.findByAuteurId(id, pageable)
                .map(LivreMapper::versResponse);
    }

    public AuteurResponse creer(AuteurRequest request) {
        Auteur entiteACreer = auteurMapper.versEntite(request);
        Auteur entiteCree = auteurRepository.save(entiteACreer);
        return auteurMapper.versResponse(entiteCree);
    }

    public Optional<AuteurResponse> mettreAJour(Long id, AuteurRequest request) {
        return auteurRepository.findById(id)
                .map(auteurExistant -> {
                    auteurExistant.setNom(request.getNom());
                    auteurExistant.setPrenom(request.getPrenom());
                    auteurExistant.setNationalite(request.getNationalite());

                    Auteur auteurModifie = auteurRepository.save(auteurExistant);
                    return auteurMapper.versResponse(auteurModifie);
                });
    }

    public boolean supprimer(Long id) {
        if (!auteurRepository.existsById(id)) {
            return false;
        }

        auteurRepository.deleteById(id);
        return true;
    }
}
