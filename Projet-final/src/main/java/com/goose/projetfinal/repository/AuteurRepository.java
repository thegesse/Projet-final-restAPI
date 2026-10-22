package com.goose.projetfinal.repository;

import com.goose.projetfinal.model.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Long> {
    // aucune méthode à ajouter
}
