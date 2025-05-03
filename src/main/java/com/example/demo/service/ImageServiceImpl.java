package com.example.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private AzureBlobService azureBlobService;

    @Override
    public Image store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        // Upload to Azure Blob Storage
        String blobUrl = azureBlobService.uploadBlob(file);
        
        // Save metadata to database
        Image image = new Image();
        image.setName(fileName);
        image.setContentType(file.getContentType());
        image.setBlobUrl(blobUrl);
        
        return imageRepository.save(image);
    }

    @Override
    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id);
    }
    
    @Override
    public byte[] getImageData(Long id) throws IOException {
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            return azureBlobService.downloadBlob(image.getBlobUrl());
        }
        return null;
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }
} 