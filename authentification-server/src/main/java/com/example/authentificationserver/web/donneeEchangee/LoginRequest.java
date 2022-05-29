package com.example.authentificationserver.web.donneeEchangee;

import javax.validation.constraints.NotBlank;


//Permettez-moi de résumer les charges utiles pour nos RestAPI :
//– Requêtes :
//
//LoginRequest : { nom d'utilisateur, mot de passe }
//Demande d'inscription : { nom d'utilisateur, e-mail, mot de passe }
//– Réponses :
//
//JwtResponse : { jeton, type, identifiant, nom d'utilisateur, e-mail, rôles }
//MessageRéponse : { message }
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
