package com.deal.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.Assert.*;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class DealControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private UUID savedDealId;

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
    void saveInitData() throws Exception {
        String json = """
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

        MvcResult content = mockMvc.perform(put("/deal/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        String body = content.getResponse().getContentAsString().trim();
        if (body.startsWith("\"") && body.endsWith("\"")) {
            body = body.substring(1, body.length() - 1);
        }
        savedDealId = UUID.fromString(body);
        assertNotNull(savedDealId);
    }

    @Test
    void saveDeleteDealContractorTest() throws Exception {
        String json = """
            {
                "dealId":"%s",
                "name":"string",
                "inn":"string",
                "roleIds":["DRAWER"],
                "main":true
            }
        """.formatted(savedDealId);

        MvcResult saveResult = mockMvc.perform(put("/deal-contractor/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id").exists())
            .andReturn();

        String saveBody = saveResult.getResponse().getContentAsString();
        String contractorId = new ObjectMapper()
            .readTree(saveBody)
            .get("id")
            .asText();

        mockMvc.perform(delete("/deal-contractor/delete/{id}", contractorId))
            .andExpect(status().is2xxSuccessful());
        
        Boolean result = jdbcTemplate.queryForObject(
            "SELECT is_active FROM deal_contractor WHERE id = ?",
            new Object[]{UUID.fromString(contractorId)},
            Boolean.class
        );

        assertEquals(false, result);
    }

    @Test
    void AddRoleTest() throws Exception{
        String json = """
            {
                "dealId":"%s",
                "name":"string",
                "inn":"string",
                "roleIds":["DRAWER"],
                "main":true
            }
        """.formatted(savedDealId);

        MvcResult saveResult = mockMvc.perform(put("/deal-contractor/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id").exists())
            .andReturn();

        String saveBody = saveResult.getResponse().getContentAsString();

        String dealContractorId = new ObjectMapper()
            .readTree(saveBody)
            .get("id")
            .asText();

        String json2 = """
            {
                "dealContractorId": "%s",
                "roleId": "GARANT"
            }
            """.formatted(dealContractorId);
        
        mockMvc.perform(post("/contractor-to-role/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
            .andExpect(status().is2xxSuccessful());
        
        List<Map<String, Object>> roles = jdbcTemplate.queryForList(
            """
                SELECT role_id FROM contractor_to_role WHERE contractor_id = ? AND is_active = true
            """,
            UUID.fromString(dealContractorId)
        );

        assertEquals(2, roles.size());
    }

}

