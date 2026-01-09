package dev.lobzter.fileupload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileData {
    private String id;
    private String originalFileName;
    private String storedFileName;
    private String contentType;
    private Long size;
}