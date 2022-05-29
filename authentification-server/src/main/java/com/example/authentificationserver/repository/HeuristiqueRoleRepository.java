package com.example.authentificationserver.repository;

import com.example.authentificationserver.entities.ERole;
import com.example.authentificationserver.entities.HeuristiqueRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeuristiqueRoleRepository extends JpaRepository<HeuristiqueRole,Long> {
    //  Methode qui permet de retourner un role avec le nom du role
    Optional<HeuristiqueRole> findByRoleName(ERole roleName);
}
