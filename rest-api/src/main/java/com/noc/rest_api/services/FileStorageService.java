package com.noc.rest_api.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.noc.rest_api.config.FileStorageConfig;
import com.noc.rest_api.exception.FileNotFoundException;
import com.noc.rest_api.exception.FileStorageException;

@Service
public class FileStorageService {
    
    private final Path fileStorageLocation;

    private final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    public FileStorageService(FileStorageConfig fileStorage){
        Path path = Paths.get(fileStorage.getUploadDir()).toAbsolutePath().normalize();

        this.fileStorageLocation = path;

        try {
            logger.info("Create directory!");
            Files.createDirectories(fileStorageLocation);
        } catch (Exception e) {
            logger.error("Could not create directory when files will be stored!");
            throw new FileStorageException("Could not create directory when files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            logger.info("Save file!");
            
            if (fileName.contains("..")) {
                logger.error("Sorry! File contains a invalid path sequence " + fileName);
                throw new FileStorageException("Sorry! File contains a invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
        } catch (Exception e) {
            logger.error("Could not store file " + fileName + ". Please try Again!");
            throw new FileStorageException("Could not store file " + fileName + ". Please try Again!", e);
        }
    }

    public Resource loadFileAsResource(String fileName){
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                logger.error("File not found " + fileName);
                throw new FileNotFoundException("File not found " + fileName);
            } 

            return resource;
        } catch (Exception e) {
            logger.error("File not found " + fileName);
            throw new FileNotFoundException("File not found " + fileName, e);
        }
    }
}
