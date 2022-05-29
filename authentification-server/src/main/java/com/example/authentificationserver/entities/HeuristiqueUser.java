package com.example.authentificationserver.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Data
public class HeuristiqueUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private  String password;

    // Un utilisateur peut avoir plusieurs roles d'ou une relation 1
    // à plusieurs
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<HeuristiqueRole>   roles = new ArrayList<>();

    //L' annotation @Lob spécifie que la base de données doit stocker la propriété en tant que Large Object .
    // La columnDefinition dans l' annotation @Column définit le type de colonne pour la propriété.
    //Puisque nous allons enregistrer le tableau d'octets , nous utilisons BLOB.

//    @Column(name = "photo", columnDefinition="BLOB")
    @Lob
    @Column(name = "photo")
    private byte[] photoProfile;
    private String photoName;
    private String photoType;

    public HeuristiqueUser(String username, String encode) {
        this.username = username;
        this.password = encode;
    }
}
