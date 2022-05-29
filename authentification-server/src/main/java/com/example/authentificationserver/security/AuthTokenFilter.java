package com.example.authentificationserver.security;


import com.example.authentificationserver.security.service.UserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// filtrer les demandes
//Définissons un filtre qui s'exécute une fois par requête.
// Nous créons donc une AuthTokenFilterclasse qui étend OncePerRequestFilteret
// redefinie la doFilterInternal()méthode.
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

   //Ce que nous faisons à l'intérieurdoFilterInternal() :
   //- obtenir JWTde l'en-tête Authorization (en supprimant le Bearerpréfixe)
   //- si la requête a JWT, la valider, l'analyser - à partir de username, créer un objet - définir le courant dans SecurityContext à l' aide de la méthode.
   //usernameUserDetailsAuthentication UserDetailssetAuthentication(authentication)
    // cette fonction s'zxecute à chaque requete de l'utilisateur
    // c'est pourquoi dès le lancement de l'application elle va generer des erreures pour un debut si l'utilisateur n'est
    // pas connecté
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        try {
            String jwt = parseJwt(request);
            if (jwt!=null && jwtUtils.validateJwtToken(jwt))
               username = jwtUtils.getUserNameFromJwtToken(jwt);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            // garde les autres parametres de la requete dans authentication
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // garde authentication de l'utilisateur courant dans le contexte de l'application
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e){
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        // requete et reponse renvoyee
//        Après cela, chaque fois que vous
//        voulez obtenir UserDetails, utilisez
//        simplement SecurityContext comme ceci :
//
//UserDetails userDetails =//	(UserDetails)
// SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        filterChain.doFilter(request,response);
    }





//    pour parsser les requetes
    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");

        // retourner la suite du Header qui suit Bearear
        if (StringUtils.hasText(headerAuth)&& headerAuth.startsWith("Bearer")){
            return headerAuth.substring(7,headerAuth.length());
    }
        return  null;
    }
}
