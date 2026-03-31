package com.noc.rest_api.file.exporter.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.noc.rest_api.file.exporter.MediaTypes;
import com.noc.rest_api.file.exporter.contract.FileExporter;
import com.noc.rest_api.file.exporter.implementation.CsvExporter;
import com.noc.rest_api.file.exporter.implementation.XlsxExporter;

@Component
public class FileExporterFactory {
    
    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileExporter getExporter(String acceptHeader) throws Exception{
        logger.info("Get type file import");

        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsopported file extension!");
        }
    }
}
