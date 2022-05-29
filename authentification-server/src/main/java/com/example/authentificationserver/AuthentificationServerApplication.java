package com.example.authentificationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthentificationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthentificationServerApplication.class, args);
	}

//	 definition de l'encoder des mots de passes, on cree un bean
//  qui va se charger d'encoder les mots de passses
@Bean
PasswordEncoder passwordEncoder(){
	return new BCryptPasswordEncoder();
}


}
