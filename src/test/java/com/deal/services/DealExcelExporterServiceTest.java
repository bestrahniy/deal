package com.deal.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.deal.Dto.ResponseDealByIdDto;

@ExtendWith(MockitoExtension.class)
public class DealExcelExporterServiceTest {

    @InjectMocks
    private DealExcelExporterService dealExcelExporterService;

    @Test
    void exportDealsTest() throws IOException {

        List<ResponseDealByIdDto> deals = prepareTestDeals();
        Page<ResponseDealByIdDto> page = new PageImpl<>(deals, PageRequest.of(0, 10), deals.size());

        Resource result = dealExcelExporterService.exportDeals(page);

        assertNotNull(result);
        assertTrue(result.contentLength() > 0);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result.getContentAsByteArray()))) {
            Sheet sheet = workbook.getSheetAt(0);


            Row headerRow = sheet.getRow(0);
            assertEquals("ИД сделки", headerRow.getCell(0).getStringCellValue());
            assertEquals("Описание", headerRow.getCell(1).getStringCellValue());
        }
    }

    private List<ResponseDealByIdDto> prepareTestDeals() {
        ResponseDealByIdDto deal1 = new ResponseDealByIdDto();
        deal1.setId(UUID.fromString("22F31789-c3e6-4175-b626-217e68dc6544"));
        deal1.setDescription("Сделка №1");
        deal1.setAgreement_number("1234-12345");
        deal1.setAgreement_date("01.01.2025");
        deal1.setAgreement_start_dt("02.01.2025");
        deal1.setAvailability_date("01.01.2026");
        
        ResponseDealByIdDto.DealTypeDto type1 = new ResponseDealByIdDto.DealTypeDto();
        type1.setName("Кредитная");
        deal1.setType(type1);
        
        ResponseDealByIdDto.DealStatusDto status1 = new ResponseDealByIdDto.DealStatusDto();
        status1.setName("Утвержденная");
        deal1.setStatus(status1);
        
        ResponseDealByIdDto.ContractorDto contractor1 = new ResponseDealByIdDto.ContractorDto();
        contractor1.setName("ИП Петр Иван Викторович");
        contractor1.setContractorId("CTR-001");
        
        ResponseDealByIdDto.RoleDto role1 = new ResponseDealByIdDto.RoleDto();
        role1.setName("Заемщик");
        contractor1.setRoles(Arrays.asList(role1));
        deal1.setContractors(Arrays.asList(contractor1));

        ResponseDealByIdDto deal2 = new ResponseDealByIdDto();
        deal2.setId(UUID.fromString("8c9f66c0-f088-48ed-bf51-4be4959733ec"));
        deal2.setDescription("Сделка №2");
        deal2.setAgreement_number("564556-344");
        deal2.setAgreement_date("01.01.2024");
        deal2.setAgreement_start_dt("02.01.2024");
        deal2.setAvailability_date("01.01.2025");
        
        ResponseDealByIdDto.DealTypeDto type2 = new ResponseDealByIdDto.DealTypeDto();
        type2.setName("Кредитная");
        deal2.setType(type2);
        
        ResponseDealByIdDto.DealStatusDto status2 = new ResponseDealByIdDto.DealStatusDto();
        status2.setName("Закрытая");
        deal2.setStatus(status2);
        
        ResponseDealByIdDto.ContractorDto contractor2 = new ResponseDealByIdDto.ContractorDto();
        contractor2.setName("ООО Рога и копыта");
        contractor2.setContractorId("CTR-002");
        
        ResponseDealByIdDto.RoleDto role2 = new ResponseDealByIdDto.RoleDto();
        role2.setName("Поручитель");
        contractor2.setRoles(Arrays.asList(role2));
        deal2.setContractors(Arrays.asList(contractor2));

        return Arrays.asList(deal1, deal2);
    }
}
