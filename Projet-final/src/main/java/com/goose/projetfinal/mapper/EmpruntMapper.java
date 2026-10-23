package com.goose.projetfinal.mapper;

import com.goose.projetfinal.dto.EmpruntRequest;
import com.goose.projetfinal.dto.EmpruntResponse;
import com.goose.projetfinal.model.Emprunt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class EmpruntMapper {
    private EmpruntMapper() {}

    public static PagedModel<EmpruntResponse> versPage(Page<Emprunt> page) {
        List<EmpruntResponse> contenu = versResponses(page.getContent());
        Page<EmpruntResponse> pageDto = new PageImpl<>(contenu, page.getPageable(), page.getTotalElements());
        return new PagedModel<>(pageDto);
    }

    public static Emprunt versEntite(EmpruntRequest request) {
        Emprunt emprunt = new Emprunt();
        emprunt.setLivreId(request.getLivreId());
        emprunt.setEmprunteur(request.getEmprunteur());
        emprunt.setEmailEmprunteur(request.getEmailEmprunteur());
        return emprunt;
    }

    public static EmpruntResponse versResponse(Emprunt emprunt) {
        return new  EmpruntResponse(
                emprunt.getId(),
                emprunt.getLivreId(),
                emprunt.getEmprunteur(),
                emprunt.getDateEmprunt(),
                emprunt.getDateRetourPrevue(),
                emprunt.getDateRetourEffective(),
                emprunt.getRendu(),
                //pour savoir si il est en retard, car il y a pas dans entite emprunt
                Boolean.FALSE.equals(emprunt.getRendu())
                        && emprunt.getDateRetourPrevue().isBefore(LocalDate.now())
        );
    }

    public static List<EmpruntResponse> versResponses(List<Emprunt> emprunts) {
        List<EmpruntResponse> contenu = new ArrayList<>();
        for (Emprunt emprunt : emprunts) {
            contenu.add(versResponse(emprunt));
        }
        return contenu;
    }
}
