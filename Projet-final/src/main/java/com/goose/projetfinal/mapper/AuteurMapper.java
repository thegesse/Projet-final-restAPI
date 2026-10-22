package com.goose.projetfinal.mapper;

import com.goose.projetfinal.dto.AuteurRequest;
import com.goose.projetfinal.dto.AuteurResponse;
import com.goose.projetfinal.model.Auteur;
import org.hibernate.mapping.Array;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.ArrayList;
import java.util.List;

public final class AuteurMapper {

    private AuteurMapper() {
    }
    public static PagedModel<AuteurResponse> versPage(Page<Auteur> page) {
        // 1. On convertit le contenu de la page en DTOs
        List<AuteurResponse> contenu = versResponses(page.getContent());
        // 2. On reconstruit une page de DTOs avec les mêmes informations de pagination
        Page<AuteurResponse> pageDto = new PageImpl<>(contenu, page.getPageable(), page.getTotalElements());
        // 3. PagedModel produit un JSON stable : { "content": [...], "page": {...} }
        return new PagedModel<>(pageDto);
    }

    public static Auteur versEntite(AuteurRequest request) {
        Auteur auteur = new Auteur();
        auteur.setNom(request.getNom());
        auteur.setPrenom(request.getPrenom());
        auteur.setNationalite(request.getNationalite());
        return auteur;
    }

    public static AuteurResponse versResponse(Auteur auteur) {
        return new AuteurResponse(
                auteur.getId(),
                auteur.getNom(),
                auteur.getPrenom(),
                auteur.getNationalite()
        );
    }

    public static List<AuteurResponse> versResponses(List<Auteur> auteurs) {
        List<AuteurResponse> contenu = new ArrayList<>();

        for(Auteur auteur : auteurs) {
            contenu.add(versResponse(auteur));
        }
        return contenu;
    }
}
