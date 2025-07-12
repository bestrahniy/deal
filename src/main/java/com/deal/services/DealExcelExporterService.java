package com.deal.services;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.deal.Dto.ResponseDealByIdDto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class DealExcelExporterService {

    public Resource exportDeals(Page<ResponseDealByIdDto> dto) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("сделки");
        String[] headers = {
            "ИД сделки", "Описание", "Номер договора", "Дата договора", "Дата и времы вступления соглашения в силу",
            "Срок действия сделки", "Тип сделики", "Статус сделки", "Сумма сделки", "Наименование валюты",
            "Основная сумма сделки", "Наименование контрагента", "ИНН контрагента", "Роли контрагента"
        };

        Row headerRow = sheet.createRow(0);
        CellStyle headCellStyle = headerSyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headCellStyle);
                }
        int rowNum = 1;
        for (ResponseDealByIdDto deal : dto.getContent()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(deal.getId().toString());
            row.createCell(1).setCellValue(deal.getDescription() != null ? deal.getDescription() : "-");
            row.createCell(2).setCellValue(deal.getAgreement_number() != null ? deal.getAgreement_number() : "-");
            row.createCell(3).setCellValue(deal.getAgreement_date() != null ? deal.getAgreement_date() : "-");
            row.createCell(4).setCellValue(deal.getAgreement_start_dt() != null ? deal.getAgreement_start_dt() : "-");
            row.createCell(5).setCellValue(deal.getAvailability_date() != null ? deal.getAvailability_date() : "-");
            row.createCell(6).setCellValue(deal.getType() != null ? deal.getType().getName() : "-");
            row.createCell(7).setCellValue(deal.getStatus() != null ? deal.getStatus().getName() : "-");

            if (deal.getContractors() != null && !deal.getContractors().isEmpty()) {
                for (ResponseDealByIdDto.ContractorDto contractor : deal.getContractors()) {
                    Row contractorRow = sheet.createRow(rowNum++);
                    contractorRow.createCell(0).setCellValue(deal.getId().toString());
                    contractorRow.createCell(8).setCellValue(contractor.getName() != null ? contractor.getName() : "-");
                    contractorRow.createCell(9).setCellValue(contractor.getContractorId() != null ? contractor.getContractorId() : "-");
                    String roles = "";
                    if (contractor.getRoles() != null) {
                        roles = contractor.getRoles().stream()
                            .map(role -> role.getName() != null ? role.getName() : "")
                            .collect(Collectors.joining(", "));
                    }
                    contractorRow.createCell(10).setCellValue(roles.isEmpty() ? "-" : roles);
                }
            } else {
                Row emptyRow = sheet.createRow(rowNum++);
                emptyRow.createCell(8).setCellValue("-");
                emptyRow.createCell(9).setCellValue("-");
                emptyRow.createCell(10).setCellValue("-");
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 500);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new ByteArrayResource(outputStream.toByteArray());
    }

    private CellStyle headerSyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(false);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

}
