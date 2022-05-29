package com.example.authentificationserver.web;


import com.example.authentificationserver.entities.ERole;
import com.example.authentificationserver.entities.HeuristiqueRole;
import com.example.authentificationserver.entities.HeuristiqueUser;
import com.example.authentificationserver.repository.HeuristiqueRoleRepository;
import com.example.authentificationserver.repository.HeuristiqueUserRepository;
import com.example.authentificationserver.security.JwtUtils;
import com.example.authentificationserver.security.service.UserDetailsImpl;
import com.example.authentificationserver.web.donneeEchangee.JwtResponse;
import com.example.authentificationserver.web.donneeEchangee.LoginRequest;
import com.example.authentificationserver.web.donneeEchangee.MessageResponse;
import com.example.authentificationserver.web.donneeEchangee.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//Ce contrôleur fournit des API pour les actions d'enregistrement et de connexion.
//
//–/api/auth/signup
//
//vérifier existant username/email
//créer un nouveau User(avec ROLE_USERsi le rôle n'est pas spécifié)
//enregistrer Userdans la base de données en utilisantUserRepository
//–/api/auth/signin
//
//authentifier { nom d'utilisateur, mot de passe }
//mise à jour SecurityContextà l'aide d' Authenticationun objet
//produireJWT
//obtenir UserDetailsde l' Authenticationobjet
//la réponse contient JWTet UserDetailsdes données
//controllers/AuthController.java
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    HeuristiqueUserRepository heuristiqueUserRepository;
    @Autowired
    HeuristiqueRoleRepository heuristiqueRoleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // authentification
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        // mise à jour du contexte
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // generation du token
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));

    }



// fonction pour l'enregistrement

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (heuristiqueUserRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
//        if (heuristiqueUserRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
        // Create new user's account
        HeuristiqueUser user = new HeuristiqueUser(signUpRequest.getUsername()
              , encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<HeuristiqueRole> roles = new HashSet<>();

        if (strRoles == null) {
            HeuristiqueRole userRole = heuristiqueRoleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        HeuristiqueRole  adminRole =heuristiqueRoleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        HeuristiqueRole modRole = heuristiqueRoleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        HeuristiqueRole userRole = heuristiqueRoleRepository.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        heuristiqueUserRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}