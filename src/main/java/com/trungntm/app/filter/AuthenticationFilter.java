package com.trungntm.app.filter;

import com.trungntm.app.validators.RouterValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter {

    @Autowired
    private RouterValidator routerValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.debug("----- Authentication filter");
        ServerHttpRequest request = exchange.getRequest();

//        if (routerValidator.isSecured.test(request)) {
//            log.debug(request.getURI().getPath() + " is matching routerValidator");
//            if (this.isAuthMissing(request)) {
//                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
//            }
//        }
        return chain.filter(exchange);
    }

    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {

        return !request.getHeaders().containsKey("Authorization");
    }

}
