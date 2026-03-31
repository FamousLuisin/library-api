package com.noc.rest_api.file.importer.implementation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import com.noc.rest_api.dto.PersonDto;
import com.noc.rest_api.file.importer.contract.FileImporter;

@Component
public class CsvImporter implements FileImporter{
    
    @Override
    public List<PersonDto> importFile(InputStream inputStream) throws Exception {
        CSVFormat format = CSVFormat.Builder.create()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .build();

        Iterable<CSVRecord> records = format.parse(new InputStreamReader(inputStream));

        return parseRecordsToPersonDtos(records);
    }

    private List<PersonDto> parseRecordsToPersonDtos(Iterable<CSVRecord> records) {
        List<PersonDto> peopleDtos = new ArrayList<>();

        for(CSVRecord record: records){
            PersonDto person = new PersonDto();
            person.setFirstName(record.get("first_name"));
            person.setLastName(record.get("last_name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setEnabled(true);

            peopleDtos.add(person);
        }

        return peopleDtos;
    }
}
