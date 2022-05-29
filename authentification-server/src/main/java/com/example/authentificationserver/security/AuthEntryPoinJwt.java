package com.example.authentificationserver.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// gestion de l'exception d'authentification
// nous créons une AuthEntryPointJwtclasse qui
// implémente l' AuthenticationEntryPointinterface.
// Ensuite, nous redéfinissons la commence()méthode.
// Cette méthode sera déclenchée chaque fois qu'un utilisateur non
// authentifié demandera une ressource HTTP sécurisée et
// qu'un an AuthenticationException sera lancé
@Component
public class AuthEntryPoinJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPoinJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
// HttpServletResponse.SC_UNAUTHORIZEDest
// le code d'état 401 . Il indique
// que la requête nécessite une authentification HTTP.