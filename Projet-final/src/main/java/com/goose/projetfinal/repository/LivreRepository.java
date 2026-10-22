package com.goose.projetfinal.repository;

import com.goose.projetfinal.model.Livre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {

    Optional<Livre> findByIsbn(String isbn);                                      // le livre qui a cet ISBN

    boolean existsByAuteurId(Long auteurId);                                      // true si l'auteur a au moins
                                                                                  // un livre

    Page<Livre> findByAuteurId(Long auteurId, Pageable pageable);                 // les livres d'un auteur

    Page<Livre> findByTitreContainingIgnoreCase(String titre, Pageable pageable); // titre contenant le texte

    Page<Livre> findByStockGreaterThan(Integer stock, Pageable pageable);         // stock strictement supérieur
}
