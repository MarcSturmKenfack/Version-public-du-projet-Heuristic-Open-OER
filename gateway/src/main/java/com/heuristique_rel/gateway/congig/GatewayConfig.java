package com.heuristique_rel.gateway.congig;


import com.heuristique_rel.gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator route(RouteLocatorBuilder builder){
//        le filtre va demender l'authorisation pour toutes les requetes entrantes sauf cette contenant register et signup
        return  builder.routes().route("auth",r->r.path("/api/auth/**").uri("lb://AUTHENTIFICATION-SERVICE")).build();
    }
}
