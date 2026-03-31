package com.noc.rest_api.file.exporter.implementation;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.file.exporter.contract.FileExporter;

@Component
public class CsvExporter implements FileExporter {
    
    @Override
    public Resource exportFile(List<PersonDto> people) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat format = CSVFormat.Builder.create()
            .setHeader("Id", "First Name", "Last Name", "Address", "Gender", "Enabled")
            .setSkipHeaderRecord(false)
            .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {          
            for (PersonDto person : people) {
                csvPrinter.printRecord(
                    person.getId(), 
                    person.getFirstName(), 
                    person.getLastName(),
                    person.getAddress(),
                    person.getGender(),
                    person.getEnabled()
                );
            }
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }

}
