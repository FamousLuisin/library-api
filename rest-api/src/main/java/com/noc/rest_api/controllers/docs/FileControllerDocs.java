package com.noc.rest_api.controllers.docs;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.noc.rest_api.dto.UploadFileResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "File", description = "File Endpoint")
public interface FileControllerDocs {
    
    @Operation(
        summary = "Upload file", description = "Upload file",
        responses = {
            @ApiResponse(description =  "Success", content = @Content(schema = @Schema(implementation = UploadFileResponseDto.class)) ,responseCode = "200"),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public UploadFileResponseDto uploadFile(MultipartFile file);

    @Operation(
        summary = "Upload files", description = "Upload files",
        responses = {
            @ApiResponse(description = "Success", content = {
                @Content(array = @ArraySchema(schema = @Schema(implementation = UploadFileResponseDto.class)))
            }, responseCode = "200"),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public List<UploadFileResponseDto> uploadMultipleFile(MultipartFile[] files);

    @Operation(
        summary = "Download file", description = "Download file",
        responses = {
            @ApiResponse(description =  "Success", content = @Content(schema = @Schema(implementation = ResponseEntity.class)) ,responseCode = "200"),
            @ApiResponse(description = "No Content", content = @Content, responseCode = "204"),
            @ApiResponse(description = "Bad Request", content = @Content, responseCode = "400"),
            @ApiResponse(description = "Unauthorized", content = @Content, responseCode = "401"),
            @ApiResponse(description = "Not Found", content = @Content, responseCode = "404"),
            @ApiResponse(description = "Internal Server Error", content = @Content, responseCode = "500")
        }
    )
    public ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request);
}
