package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import com.example.demo.model.Image;
import com.example.demo.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Image API", description = "API for uploading and viewing images with Azure Blob Storage")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    @Operation(summary = "Upload an image to Azure Blob Storage")
    public ResponseEntity<UploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Image image = imageService.store(file);
            
            // Return both API URL and direct Blob URL
            String apiUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/images/")
                .path(image.getId().toString())
                .toUriString();
                
            UploadResponse response = new UploadResponse(
                image.getId(), 
                apiUrl, 
                image.getBlobUrl(),
                "Image uploaded successfully"
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UploadResponse(null, null, null, "Could not upload the image: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an image by ID from Azure Blob Storage")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        try {
            byte[] imageData = imageService.getImageData(id);
            if (imageData == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Image image = imageService.getImage(id).orElse(null);
            if (image == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(image.getContentType()));
            
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @Operation(summary = "Get all image metadata")
    public ResponseEntity<List<ImageDto>> getAllImages() {
        List<ImageDto> images = imageService.getAllImages().stream()
            .map(image -> {
                String apiUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/images/")
                    .path(image.getId().toString())
                    .toUriString();
                
                return new ImageDto(
                    image.getId(),
                    image.getName(),
                    image.getContentType(),
                    apiUrl,
                    image.getBlobUrl()  // This is the direct URL to the blob
                );
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(images);
    }
    
    // DTO for returning image metadata
    record ImageDto(Long id, String name, String contentType, String apiUrl, String blobUrl) {}
    
    // Response for upload
    record UploadResponse(Long id, String apiUrl, String blobUrl, String message) {}
} 