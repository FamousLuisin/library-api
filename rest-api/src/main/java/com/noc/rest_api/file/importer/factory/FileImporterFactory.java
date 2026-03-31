package com.noc.rest_api.file.importer.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.noc.rest_api.file.importer.contract.FileImporter;
import com.noc.rest_api.file.importer.implementation.CsvImporter;
import com.noc.rest_api.file.importer.implementation.XlsxImporter;

@Component
public class FileImporterFactory {
    
    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileImporter getImporter(String fileName) throws Exception{
        logger.info("Get type file import");

        if (fileName.endsWith(".xlsx")) {
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return context.getBean(CsvImporter.class);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsopported file extension!");
        }
    }
}
