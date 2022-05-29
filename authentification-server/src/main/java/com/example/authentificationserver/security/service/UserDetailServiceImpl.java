package com.example.authentificationserver.security.service;

import com.example.authentificationserver.entities.HeuristiqueUser;
import com.example.authentificationserver.repository.HeuristiqueUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//  c'est cette qui est censé aller cher l'utilisateur en bd
// et leur roles afin de creer une session
// la methode prend le usename et recupere l'utilisateur en bd
// sous la forme heuristiqueUserdetail , ses roles , id, etc..
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    HeuristiqueUserRepository heuristiqueUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HeuristiqueUser heuristiqueUser = heuristiqueUserRepository.findByUsername(username);
        return UserDetailsImpl.build(heuristiqueUser);
    }
}
