package com.example.authentificationserver.repository;

import com.example.authentificationserver.entities.HeuristiqueUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeuristiqueUserRepository extends JpaRepository<HeuristiqueUser,Long> {
    //    Methode qui retourne un utilisateur dont connait son nom
    // nous allons ajouter des methodes au Repository en faisant un ajout des methodes personnalises par convention

    HeuristiqueUser findByUsername(String username);

    // verifier si un utilisateur existe en BD
    Boolean existsHeuristiqueUserByUsername(String username);

    boolean existsByUsername(String username);
}
