package com.packt.footballresource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        // .requestMatchers(HttpMethod.GET, "/football/teams/**").hasAnyAuthority("APPROLE_football.read", "APPROLE_football.admin")
                        // .requestMatchers(HttpMethod.POST, "/football/teams/**").hasAnyAuthority("APPROLE_football.admin")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    // @Bean
    // public Converter<Jwt, Collection<GrantedAuthority>> aadJwtGrantedAuthoritiesConverter() {
    //     return new AadJwtGrantedAuthoritiesConverter();
    // }

    // @Bean
    // public JwtAuthenticationConverter aadJwtAuthenticationConverter() {
    //     JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    //     converter.setJwtGrantedAuthoritiesConverter(aadJwtGrantedAuthoritiesConverter());
    //     return converter;
    // }
}
