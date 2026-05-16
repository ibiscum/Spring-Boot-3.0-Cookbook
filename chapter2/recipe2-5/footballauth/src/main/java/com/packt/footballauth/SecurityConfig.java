package com.packt.footballauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        // 1. NEU: Instanzieren Sie den Configurer manuell, um auf seine Routen-Matcher zuzugreifen
        OAuth2AuthorizationServerConfigurer authServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        http
            // 2. Grenzt diese Kette exklusiv auf die vordefinierten OAuth2-Endpunkte ein
            .securityMatcher(authServerConfigurer.getEndpointsMatcher())

            // 3. Übergibt den vorkonfigurierten Configurer per Lambda-DSL an die Kette
            .with(authServerConfigurer, authorizationServer -> authorizationServer
                .oidc(Customizer.withDefaults()) // Aktiviert OpenID Connect 1.0
            )
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            )
            .exceptionHandling(exceptions -> exceptions
                .defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/login"),
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // Kette 2 fungiert als globales "Catch-All" für Standard-Webseiten und Logins
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())
            .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}
