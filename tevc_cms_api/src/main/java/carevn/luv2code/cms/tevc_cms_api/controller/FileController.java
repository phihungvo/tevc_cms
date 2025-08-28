package carevn.luv2code.cms.tevc_cms_api.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import carevn.luv2code.cms.tevc_cms_api.configuration.MinioService;
import io.minio.StatObjectResponse;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty. Please upload a valid file.");
        }
        String fileName = minioService.uploadFile(file);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        try (InputStream inputStream = minioService.downloadFile(fileName)) {
            byte[] fileContent = inputStream.readAllBytes();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading file: " + fileName + ". " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        if (!minioService.fileExists(fileName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + fileName);
        }
        minioService.deleteFile(fileName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        List<String> fileNames = minioService.listFiles();
        return ResponseEntity.ok(fileNames);
    }

    @GetMapping("/exists/{fileName}")
    public ResponseEntity<Boolean> fileExists(@PathVariable String fileName) {
        boolean exists = minioService.fileExists(fileName);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/metadata/{fileName}")
    public ResponseEntity<StatObjectResponse> getFileMetadata(@PathVariable String fileName) {
        StatObjectResponse metadata = minioService.getFileMetadata(fileName);
        return ResponseEntity.ok(metadata);
    }

    @PostMapping("/copy")
    public ResponseEntity<Void> copyFile(@RequestParam String sourceFileName, @RequestParam String targetFileName) {
        if (sourceFileName.isBlank() || targetFileName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        minioService.copyFile(sourceFileName, targetFileName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/move")
    public ResponseEntity<Void> moveFile(@RequestParam String sourceFileName, @RequestParam String targetFileName) {
        if (sourceFileName.isBlank() || targetFileName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        minioService.moveFile(sourceFileName, targetFileName);
        return ResponseEntity.ok().build();
    }
}
