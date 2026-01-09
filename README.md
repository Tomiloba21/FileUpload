# File Upload Service

A RESTful File Upload API built with Spring Boot that allows users to upload, retrieve, and delete files.

## 📋 Overview

This service provides a simple yet robust file management system with the following features:
- Upload files (jpg, png, pdf)
- Retrieve files by unique ID
- Delete files
- Comprehensive API documentation via Swagger

## 🛠 Tech Stack

- Java 17+
- Spring Boot 3.2.0
- Maven
- Swagger/OpenAPI 3.0

## 📁 Project Structure

```
src/main/java/com/fileupload/
├── FileUploadServiceApplication.java
├── config/
│   └── SwaggerConfig.java
├── controller/
│   └── FileController.java
├── dto/
│   ├── ErrorResponse.java
│   └── FileUploadResponse.java
├── exception/
│   ├── FileNotFoundException.java
│   ├── FileStorageException.java
│   ├── InvalidFileException.java
│   └── GlobalExceptionHandler.java
├── model/
│   └── FileMetadata.java
└── service/
└── FileStorageService.java
```

## 🚀 How to Build and Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build the Application
```bash
mvn clean install
````

### Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📌 API Endpoints

### Upload File
- **POST** \`/files/upload\`
- Upload a file with multipart/form-data
- Returns file ID and metadata

### Retrieve File
- **GET** \`/files/{id}\`
- Download a file by its unique ID
- Returns the file with appropriate headers

### Delete File (Bonus)
- **DELETE** \`/files/{id}\`
- Delete a file by its unique ID

## ⚙️ File Upload Limits

- **Allowed File Types**: jpg, jpeg, png, pdf
- **Maximum File Size**: 5 MB (5,242,880 bytes)

## 📖 Swagger Documentation

Access the interactive API documentation at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html\`
- **API Docs**: `http://localhost:8080/api-docs\`

You can test all endpoints directly from the Swagger UI interface.

## 💾 Storage

- Files are stored locally in the \`uploads/\` directory
- File metadata is stored in-memory using a ConcurrentHashMap
- Each file is assigned a unique UUID identifier
- Stored filenames are randomized to prevent conflicts

## 🔒 Security Features

- File type validation (only jpg, png, pdf allowed)
- File size validation (max 5MB)
- Path traversal attack prevention
- Unique filename generation
- Content-Type validation

## ✅ Validation & Error Handling

The API returns appropriate HTTP status codes:

- **200 OK**: Successful operation
- **400 Bad Request**: Invalid file type or size exceeded
- **404 Not Found**: File not found
- **500 Internal Server Error**: Server-side error

Error responses include:
- Timestamp
- HTTP status code
- Error message
- Request path

## 🧪 Testing with Swagger UI

1. Navigate to `http://localhost:8080/swagger-ui.html\`
2. Expand the **POST /files/upload** endpoint
3. Click "Try it out"
4. Choose a file (jpg, png, or pdf under 5MB)
5. Click "Execute"
6. Copy the returned file ID
7. Use the ID to test the GET or DELETE endpoints

## 🔧 Configuration

All configurations can be modified in \`application.properties\`:

```properties
server.port=8080
spring.servlet.multipart.max-file-size=5MB
file.upload-dir=uploads
```