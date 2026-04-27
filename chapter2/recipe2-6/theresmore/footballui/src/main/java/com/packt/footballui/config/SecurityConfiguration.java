package com.packt.footballui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;



@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
            // 1. Aktiviert den OAuth2 Login (Azure B2C Starter konfiguriert dies im Hintergrund)
            .oauth2Login(Customizer.withDefaults())

            // 2. Zugriffsberechtigungen
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/error", "/public/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }

}
