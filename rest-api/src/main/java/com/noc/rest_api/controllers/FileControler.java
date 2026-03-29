package com.noc.rest_api.controllers;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.noc.rest_api.controllers.docs.FileControllerDocs;
import com.noc.rest_api.dto.UploadFileResponseDto;
import com.noc.rest_api.exception.FileNotFoundException;
import com.noc.rest_api.services.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/file")
public class FileControler implements FileControllerDocs{

    @Autowired
    private FileStorageService service;

    private Logger logger = LoggerFactory.getLogger(FileControler.class);

    @PostMapping(path = "/uploadFile")
    @Override
    public UploadFileResponseDto uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = service.storeFile(file);
        
        var fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/file/downloadFile/")
            .path(fileName)
            .toUriString();

        return new UploadFileResponseDto(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping(path = "/uploadMultipleFiles")
    @Override
    public List<UploadFileResponseDto> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files).stream().map(file -> uploadFile(file)).toList();
    }

    @GetMapping(path = "/downloadFile/{fileName:.+}")
    @Override
    public ResponseEntity<Resource> downloadFile(@PathVariable(value = "fileName") String fileName, HttpServletRequest request) {
        Resource resource = service.loadFileAsResource(fileName);
        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception e) {
            logger.error("Could not determine file type!");
            throw new FileNotFoundException("Could not determine file type!", e);
        }

        if (contentType == null) contentType = "application/octet-stream";
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
    
}
