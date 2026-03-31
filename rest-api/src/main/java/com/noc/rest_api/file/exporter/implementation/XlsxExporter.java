package com.noc.rest_api.file.exporter.implementation;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.file.exporter.contract.FileExporter;

@Component
public class XlsxExporter implements FileExporter {

    @Override
    public Resource exportFile(List<PersonDto> people) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("People");

            Row headeRow = sheet.createRow(0);
            String[] headers = {"Id", "First Name", "Last Name", "Address", "Gender", "Enabled"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headeRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowIndex = 1;

            for (PersonDto person : people){
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(person.getId());
                row.createCell(1).setCellValue(person.getFirstName());
                row.createCell(2).setCellValue(person.getLastName());
                row.createCell(3).setCellValue(person.getAddress());
                row.createCell(4).setCellValue(person.getGender());
                row.createCell(5).setCellValue(person.getEnabled() != null && person.getEnabled() ? "Yes" : "No");
            }

            for (int j = 0; j < headers.length; j++) {
                sheet.autoSizeColumn(j);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        } 
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

}
