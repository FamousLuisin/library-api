package com.noc.rest_api.file.importer.contract;

import java.io.InputStream;
import java.util.List;

import com.noc.rest_api.dto.PersonDto;

public interface FileImporter {
    
    List<PersonDto> importFile(InputStream inputStream) throws Exception;
}
