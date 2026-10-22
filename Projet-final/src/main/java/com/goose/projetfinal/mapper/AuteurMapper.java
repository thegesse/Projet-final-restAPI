package com.goose.projetfinal.mapper;

import com.goose.projetfinal.dto.AuteurResponse;
import com.goose.projetfinal.model.Auteur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

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

    // Écrivez ici les méthodes versEntite, versResponse et versResponses
}
