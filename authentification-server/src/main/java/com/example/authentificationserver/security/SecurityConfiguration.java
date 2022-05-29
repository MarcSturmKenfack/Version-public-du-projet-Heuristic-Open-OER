package com.example.authentificationserver.security;
import com.example.authentificationserver.security.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// classe de configuration de Spring security
//La propriété prePostEnabled active les annotations pré/post de Spring Security.
//La propriété secureEnabled détermine si l' annotation @Secured doit être activée.
//La propriété jsr250Enabled nous permet d'utiliser l' annotation @RoleAllowed .
// Les annotations @PreAuthorize et @PostAuthorize fournissent un contrôle d'accès basé sur l'expression
// L' annotation @PreAuthorize vérifie l'expression donnée avant d'entrer dans la méthode ,
// tandis que l' annotation @PostAuthorize la
// vérifie après l'exécution de la méthode et pourrait modifier le résultat.
//L' annotation @Secured est utilisée pour spécifier une liste de rôles sur une méthode
//Spring Security fournit l' annotation @PreFilter pour filtrer un argument de collection avant d'exécuter la méthode :

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // on definfie ici le fournisseur d'authentification,
    // qui est ici une table de notre bd
    // qui est ici une table de notre bd
    // on va dire à spring ou trouver les roles et les users

    @Autowired
    private AuthEntryPoinJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    //@Autowired
    //AccountService accountService;
    @Autowired
    UserDetailServiceImpl userDetailService1;
    @Autowired
    PasswordEncoder byCryptPasswordEncoder;
    //@Autowired
    //AuthenticationManager authenticationManager;
//    configuration des utilisateurs pour authentification
    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);

        //1-recherche des details de connection de l'utilisateur pour les comparer avec ce qu'il fourni
        // la fonction retourne l'utilisateur verifie avec ses roles
        // le reste des comparaisons c'est spring security qui le fait pour voir si c'est effectivement le bon
        // utilisateur
        auth.userDetailsService(userDetailService1).passwordEncoder(byCryptPasswordEncoder);

    }


    // configuration pour autoriser les requêtes et l'accès aux resources
    @Override
//    TODO: revenir sur la configuration des requetes
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http.cors().and().csrf().disable()
                // definition du gestionnaire d'exception
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // pour dire que nous definissons une authentification basee  StateLESS
                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test/**").permitAll()
                .anyRequest().authenticated();
// definition du filtre , et definissons quand quand nous voulons que ik fonctionne avant
        // addFilterBefore(filter, class) – ajoute un filtre avant
        // la position de la classe de filtre spécifiée
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    // TODO: Va faloir configurer pour demain les filtres au niveau de Spring Boot pour la securite

    // Been pour l'authentification Managger,
    // en tant que objet du contexte spring
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}

