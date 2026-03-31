package com.noc.rest_api.file.exporter.contract;

import java.util.List;

import org.springframework.core.io.Resource;

import com.noc.rest_api.dto.PersonDto;

public interface FileExporter {
    
    Resource exportFile(List<PersonDto> people) throws Exception;
}
