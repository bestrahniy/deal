package com.deal.controller.Uicontrollers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import com.deal.Dto.UserRolesDto;
import com.deal.services.AuthConnect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import static org.mockito.BDDMockito.given;

@SpringBootTest(
    properties = {
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:0/.well-known/jwks.json",
        "security.jwt.secret_key=b55a3f4a6400c4ef85c16187653713004986bace196af0d78c24b0d1ca26cd9a"
    }
)
@AutoConfigureMockMvc
@Testcontainers
public class UiSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthConnect authConnect;

    private Key key;

    private int accessTokenExpiration = 3600000;

    private String secretKey = "b55a3f4a6400c4ef85c16187653713004986bace196af0d78c24b0d1ca26cd9a";

    @Container
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.liquibase.change-log", () ->  "classpath:db/changelog/db.changelog-master.yaml");
    }

    @BeforeEach
    void init() throws DecoderException {
        byte[] keyBytes = org.apache.commons.codec.binary.Hex.decodeHex(secretKey.toCharArray());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    @TestConfiguration
    static class JwtDecoderConfig {

        private String secretKey = "b55a3f4a6400c4ef85c16187653713004986bace196af0d78c24b0d1ca26cd9a";

        @Bean
        public JwtDecoder init() throws DecoderException {
            byte[] keyBytes = org.apache.commons.codec.binary.Hex
                            .decodeHex(secretKey.toCharArray());
            return NimbusJwtDecoder
                    .withSecretKey(new javax.crypto.spec.SecretKeySpec(keyBytes, "HmacSHA256"))
                    .build();
        }
    }

    private String createToken(Set<String> roles) {
        List<String> rolesName = roles.stream().toList();
        Instant now = Instant.now();
        return Jwts.builder()
                .issuedAt(Date.from(now))
                .notBefore(Date.from(now))
                .expiration(Date.from(now.plusMillis(accessTokenExpiration)))
                .signWith(key)
                .subject("testUser")
                .claim("roles", rolesName)
                .compact();
    }

    @Test
    void whenNoToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/ui/deals"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void whenValidToken_thenAccessDeals() throws Exception{
        String token = createToken(Set.of("USER"));

        mockMvc.perform(get("/ui/deals")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }

    @Test
    void whenSaveDealBySUPERUSER_thenAccess() throws Exception {
        String token = createToken(Set.of("SUPERUSER"));
        String jsonDeal = """
        {
        "description": "12",
        "agreementNumber": "12/24",
        "agreementDate": "2025-06-30",
        "agreementStartDt": "2025-06-30T12:11:34.169Z",
        "availabilityDate": "2026-06-30",
        "typeId": "CREDIT",
        "sums": [
            {
            "sum": 10000.0,
            "currencyId": "RUB",
            "main": true
            }
        ],
        "contractors": [
            {
            "contractorId": "CTR-001",
            "name": "ООО Ромашка",
            "inn": "7700000000",
            "main": true
            }
        ]
        }
            """;
        mockMvc.perform(put("/ui/deal/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDeal)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenSaveDealByUser_thenMistake() throws Exception {
        String token = createToken(Set.of("USER"));
        String jsonDeal = """
        {
        "description": "12",
        "agreementNumber": "12/24",
        "agreementDate": "2025-06-30",
        "agreementStartDt": "2025-06-30T12:11:34.169Z",
        "availabilityDate": "2026-06-30",
        "typeId": "CREDIT",
        "sums": [
            {
            "sum": 10000.0,
            "currencyId": "RUB",
            "main": true
            }
        ],
        "contractors": [
            {
            "contractorId": "CTR-001",
            "name": "ООО Ромашка",
            "inn": "7700000000",
            "main": true
            }
        ]
        }
            """;
        mockMvc.perform(put("/ui/deal/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDeal)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void whenSaveNewUserRoles_thenChangeSetUserRoles() throws JsonProcessingException, Exception {
        UserRolesDto dto = new UserRolesDto("ilya", List.of("USER", "CREDIT_USER"));

        Instant now = Instant.now();
        String adminJwt = Jwts.builder()
            .issuedAt(Date.from(now))
            .notBefore(Date.from(now))
            .expiration(Date.from(now.plusSeconds(3600)))
            .signWith(key)
            .subject("admin")
            .claim("roles", List.of("ADMIN"))
            .compact();

        given(authConnect.addRole(eq(adminJwt), refEq(dto)))
            .willReturn("TEST_ACCESS_TOKEN");

        mockMvc.perform(put("/ui/user-roles/save")
                .header("Authorization", "Bearer " + adminJwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().string("TEST_ACCESS_TOKEN"));
        then(authConnect).should().addRole(eq(adminJwt), refEq(dto));
    }

}
