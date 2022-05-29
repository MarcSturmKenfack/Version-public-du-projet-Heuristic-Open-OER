package com.heuristique_rel.gateway.filter;


//La passerelle Spring Cloud agit comme un gardien qui accepte/rejette les demandes
// des clients en fonction des critères configurés dans la passerelle.
//La partie importante de la passerelle est le filtre qui effectue la validation des requêtes entrantes et achemine les requêtes vers les microservices appropriés.
//Dans le code source ci-dessous, j'ai contourné
// les points de terminaison
// /register et /login du contrôle de sécurité.
// Ainsi, ces points de terminaison peuvent être facilement accessibles sans fournir aucune autorité.

import com.heuristique_rel.gateway.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

//        une errow fonction predicate
        final List<String> apiEndpoints = List.of("/signin","/signup");
        Predicate<ServerHttpRequest> isApiSecured = r->apiEndpoints.stream()
                .noneMatch(uri->r.getURI().getPath().contains(uri));

        if(isApiSecured.test(request)){
            // TODO : à revenir dessus
            if (!request.getHeaders().containsKey("Authorization")){

//                modifier la reponse du serveur , renvoie non authorise au cas ou
                ServerHttpResponse reponse = exchange.getResponse();
                reponse.setStatusCode(HttpStatus.UNAUTHORIZED);

                return  reponse.setComplete();

            }

            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            try {
                jwtUtils.validateJwtToken(token);
            }catch (Exception e){

                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);

                return response.setComplete();
            }

            // on ajoute le username dans le geader de la requete
            String userName = jwtUtils.getUserNameFromJwtToken(token);
            exchange.getRequest().mutate().header("userName",userName);
        }

        return chain.filter(exchange);
    }
}
