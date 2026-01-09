package dev.lobzter.fileupload.exceptions;



public class FileStorageExceptions extends RuntimeException  {
    public FileStorageExceptions(String message) {
        super(message);
    }

    public FileStorageExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
