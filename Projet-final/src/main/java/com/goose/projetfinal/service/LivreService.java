package com.goose.projetfinal.service;

import com.goose.projetfinal.model.Livre;
import com.goose.projetfinal.repository.AuteurRepository;
import com.goose.projetfinal.repository.LivreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class LivreService {

    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;

    public LivreService(LivreRepository livreRepository, AuteurRepository auteurRepository) {
        this.livreRepository = livreRepository;
        this.auteurRepository = auteurRepository;
    }

    // Écrivez ici les méthodes du tableau
    public Page<Livre> recupererTous(Pageable pageable) {
        return livreRepository.findAll(pageable);
    }

    public boolean existe(Long id) {return livreRepository.existsById(id);}

    public boolean auteurExiste(Long id) {
        return auteurRepository.existsById(id);
    }

    public boolean  isbnExiste(String isbn) {
        return livreRepository.findByIsbn(isbn).isPresent();
    }

    public boolean isbnUtiliseParUnAutreLivre(String isbn, Long livreId) {
        return livreRepository.findByIsbn(isbn)
                .map(livre -> !livre.getId().equals(livreId))
                .orElse(false);

    }

    public Page<Livre> rechercherParTitre(String titre, Pageable pageable) {
        return livreRepository.findByTitreContainingIgnoreCase(titre, pageable);
    }

    public Page<Livre> recupererDisponibles(Pageable pageable) {
        return livreRepository.findByStockGreaterThan(0, pageable);
    }

    public Livre recupererParId(Long id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Livre ajouterStock(Livre livre, Integer quantite) {
        if (quantite == null || quantite <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité doit être positive");
        }
        livre.setStock(livre.getStock() + quantite);
        return livreRepository.save(livre);
    }

    public Page<Livre> recupererParAuteurId(Long id, Pageable pageable) {
        if (!auteurRepository.existsById(id)) {
            throw new EntityNotFoundException("Auteur non trouvable");
        }
        return livreRepository.findByAuteurId(id, pageable);
    }

    public Livre creer(Livre livre) {
        if (!auteurExiste(livre.getAuteurId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auteur inexistant");
        }
        if (isbnExiste(livre.getIsbn())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN déjà utilisé");
        }
        livre.setId(null);
        return livreRepository.save(livre);
    }

    public Livre mettreAJour(Long id, Livre nouvellesValeurs) {
        Livre livreExistant = livreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Livre inexistant"));

        if (!auteurExiste(nouvellesValeurs.getAuteurId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Auteur inexistant");
        }

        if (isbnUtiliseParUnAutreLivre(nouvellesValeurs.getIsbn(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "ISBN déjà utilisé");
        }

        livreExistant.setTitre(nouvellesValeurs.getTitre());
        livreExistant.setIsbn(nouvellesValeurs.getIsbn());
        livreExistant.setPrix(nouvellesValeurs.getPrix());
        livreExistant.setAnneePublication(nouvellesValeurs.getAnneePublication());
        livreExistant.setStock(nouvellesValeurs.getStock());
        livreExistant.setAuteurId(nouvellesValeurs.getAuteurId());

        return livreRepository.save(livreExistant);
    }

    public void supprimer(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livre non trouvable");
        }
        livreRepository.deleteById(id);
    }
}

