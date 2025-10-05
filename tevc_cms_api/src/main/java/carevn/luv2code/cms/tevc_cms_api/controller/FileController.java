package carevn.luv2code.cms.tevc_cms_api.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import carevn.luv2code.cms.tevc_cms_api.configuration.MinioService;
import carevn.luv2code.cms.tevc_cms_api.dto.FileDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.response.ApiResponse;
import carevn.luv2code.cms.tevc_cms_api.entity.File;
import carevn.luv2code.cms.tevc_cms_api.entity.User;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.repository.FileRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.UserRepository;
import carevn.luv2code.cms.tevc_cms_api.service.EmployeeService;
import io.minio.StatObjectResponse;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private MinioService minioService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @PostMapping("/upload")
    public ApiResponse<FileDTO> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam Integer employeeId) {
        if (file.isEmpty()) {
            return ApiResponse.<FileDTO>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("File is empty. Please upload a valid file.")
                    .result(null)
                    .build();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User uploadedBy = userRepository
                .findByUserName(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        FileDTO savedFile = minioService.uploadFileForEmployee(file, uploadedBy, employeeId);
        return ApiResponse.<FileDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Upload successful")
                .result(savedFile)
                .build();
    }

    @PostMapping("/upload/contract/{contractId}")
    public ApiResponse<List<FileDTO>> uploadFilesForContract(
            @PathVariable Integer contractId, @RequestParam("files") MultipartFile[] files) {

        if (files == null || files.length == 0) {
            return ApiResponse.<List<FileDTO>>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Không có file nào được chọn")
                    .result(null)
                    .build();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User uploadedBy = userRepository
                .findByUserName(auth.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<FileDTO> savedFiles = minioService.uploadFilesForContract(files, uploadedBy, contractId);

        return ApiResponse.<List<FileDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Upload file cho hợp đồng thành công")
                .result(savedFiles)
                .build();
    }

    @GetMapping("/presigned-url/{fileId}")
    public ResponseEntity<ApiResponse<String>> getPresignedUrl(@PathVariable Integer fileId) {
        try {
            File existingFile =
                    fileRepository.findById(fileId).orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));
            String fileName = existingFile.getFileName();
            if (!minioService.fileExists(fileName)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.<String>builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .message("File not found: " + fileName)
                                .result(null)
                                .build());
            }
            String presignedUrl = minioService.generatePresignedUrl(fileName);
            return ResponseEntity.ok(ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Presigned URL generated successfully")
                    .result(presignedUrl)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<String>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error generating presigned URL: " + e.getMessage())
                            .result(null)
                            .build());
        }
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
