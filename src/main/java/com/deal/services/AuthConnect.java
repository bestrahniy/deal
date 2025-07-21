package com.deal.services;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.deal.Dto.UserRolesDto;

/**
 * service for connect with auth service
 */
@Service
public class AuthConnect {

    private final RestTemplate restTemplate;

    public AuthConnect(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate
            .connectTimeout(Duration.ofSeconds(5))
            .readTimeout(Duration.ofSeconds(5))
            .build();
    }

    public String addRole(String currentAdminToken, UserRolesDto userRolesDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(currentAdminToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserRolesDto> request = new HttpEntity<>(userRolesDto, httpHeaders);
        return restTemplate.exchange(
            "http://localhost:8082/auth/user-roles/save",
            HttpMethod.POST,
            request,
            String.class
            ).getBody();
    }

}
