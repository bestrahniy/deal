package com.deal.config;

import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SeurityConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter rolesConverter = new JwtGrantedAuthoritiesConverter();
        rolesConverter.setAuthorityPrefix("ROLE_");
        rolesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
        authConverter.setJwtGrantedAuthoritiesConverter(rolesConverter);
        return authConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity httpSecurity,
        JwtAuthenticationConverter jwtConverter,
        JwtDecoder jwtDecoder
        ) throws Exception {

        httpSecurity.csrf(scrf -> scrf.disable())
            .authorizeHttpRequests(request -> request
                .requestMatchers(HttpMethod.GET,
                    "/ui/contractor/county/all",
                    "/ui/deal/deal-status"
                ).hasAnyRole(
                    "USER", "CREDIT_USER", "OVERDRAFT_USER",
                    "DEAL_SUPERUSER", "CONTRACTOR_RUS", "SUPERUSER"
                )
                .requestMatchers(HttpMethod.POST,
                    "/ui/deal/search",
                    "/ui/deal/search/export"
                ).hasAnyRole(
                    "CREDIT_USER", "OVERDRAFT_USER",
                    "DEAL_SUPERUSER", "SUPERUSER"
                )
                .requestMatchers(HttpMethod.PUT,   "/ui/deal/save")
                    .hasAnyRole("DEAL_SUPERUSER", "SUPERUSER")
                .requestMatchers(HttpMethod.PATCH, "/ui/deal/change/status")
                    .hasAnyRole("DEAL_SUPERUSER", "SUPERUSER")
                .requestMatchers(HttpMethod.GET,   "/ui/deal/{id}")
                    .hasAnyRole(
                    "USER", "CREDIT_USER", "OVERDRAFT_USER",
                    "DEAL_SUPERUSER", "SUPERUSER"
                )
                .requestMatchers(HttpMethod.PUT,    "/ui/deal-contractor/save")
                    .hasAnyRole("DEAL_SUPERUSER", "SUPERUSER")
                .requestMatchers(HttpMethod.DELETE, "/ui/deal-contractor/delete")
                    .hasAnyRole("DEAL_SUPERUSER", "SUPERUSER")
                .requestMatchers(HttpMethod.POST,   "/ui/contractor-to-role/add")
                    .hasAnyRole("DEAL_SUPERUSER", "SUPERUSER")
                .requestMatchers(HttpMethod.DELETE, "/ui/contractor-to-role/delete")
                    .hasAnyRole("DEAL_SUPERUSER", "SUPERUSER")
                .requestMatchers("/ui/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/ui//user-roles/save")
                    .hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/ui/user-roles/{login}")
                    .hasAnyRole("USER", "CREDIT_USER", "OVERDRAFT_USER",
                    "DEAL_SUPERUSER", "CONTRACTOR_RUS", "SUPERUSER",
                    "ADMIN", "CONTRACTOR_SUPERUSER")
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder)
                    .jwtAuthenticationConverter(jwtConverter)
                )
            );
        return httpSecurity.build();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "security.jwt.secret_key")
    public JwtDecoder hmacJwtDecoder(
        @Value("${security.jwt.secret_key}") String secretKeyHex
    ) throws DecoderException {
        byte[] keyBytes = org.apache.commons.codec.binary.Hex.decodeHex(secretKeyHex.toCharArray());
        javax.crypto.SecretKey key = new javax.crypto.spec.SecretKeySpec(keyBytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    @ConditionalOnProperty(name = "security.oauth2.resourceserver.jwt.jwk-set-uri")
    public JwtDecoder jwkSetUriProperty(@Value("${security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkUri) {
        return NimbusJwtDecoder.withJwkSetUri(jwkUri).build();
    }

}
