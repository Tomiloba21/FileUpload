package dev.lobzter.fileupload.service;



import dev.lobzter.fileupload.exceptions.FileNotFoundException;
import dev.lobzter.fileupload.exceptions.FileStorageExceptions;
import dev.lobzter.fileupload.exceptions.InvalidFileExceptions;
import dev.lobzter.fileupload.model.FileData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    private final Map<String, FileData> fileMetadataStore = new ConcurrentHashMap<>();
    private final Set<String> allowedFileTypes = Set.of("jpg", "jpeg", "png", "pdf");
    private final long maxFileSize;

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir,
                              @Value("${file.max-size:5242880}") long maxFileSize) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.maxFileSize = maxFileSize;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageExceptions("Could not create upload directory", ex);
        }
    }

    public FileData storeFile(MultipartFile file) {
        validateFile(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        String storedFileName = generateUniqueFileName(fileExtension);
        String fileId = UUID.randomUUID().toString();

        try {
            if (originalFileName.contains("..")) {
                throw new InvalidFileExceptions("Invalid file path: " + originalFileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileData metadata = FileData.builder()
                    .id(fileId)
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();

            fileMetadataStore.put(fileId, metadata);

            return metadata;
        } catch (IOException ex) {
            throw new FileStorageExceptions("Could not store file " + originalFileName, ex);
        }
    }

    public Resource loadFileAsResource(String fileId) {
        FileData metadata = fileMetadataStore.get(fileId);
        if (metadata == null) {
            throw new FileNotFoundException("File not found with id: " + fileId);
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(metadata.getStoredFileName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + metadata.getStoredFileName());
            }
        } catch (Exception ex) {
            throw new FileNotFoundException("File not found with id: " + fileId);
        }
    }

    public FileData getFileMetadata(String fileId) {
        FileData metadata = fileMetadataStore.get(fileId);
        if (metadata == null) {
            throw new FileNotFoundException("File not found with id: " + fileId);
        }
        return metadata;
    }

    public void deleteFile(String fileId) {
        FileData metadata = fileMetadataStore.get(fileId);
        if (metadata == null) {
            throw new FileNotFoundException("File not found with id: " + fileId);
        }

        try {
            Path filePath = this.fileStorageLocation.resolve(metadata.getStoredFileName()).normalize();
            Files.deleteIfExists(filePath);
            fileMetadataStore.remove(fileId);
        } catch (IOException ex) {
            throw new FileStorageExceptions("Could not delete file", ex);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileExceptions("Cannot upload empty file");
        }

        if (file.getSize() > maxFileSize) {
            throw new InvalidFileExceptions("File size exceeds maximum limit of 5MB");
        }

        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName).toLowerCase();

        if (!allowedFileTypes.contains(extension)) {
            throw new InvalidFileExceptions("File type not allowed. Only jpg, png, and pdf files are accepted");
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String generateUniqueFileName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }
}