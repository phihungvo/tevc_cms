package carevn.luv2code.cms.tevc_cms_api.configuration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import io.minio.*;
import io.minio.CopySource;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @PostConstruct
    public void init() {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_INIT_ERROR);
        }
    }

    public String uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucket).object(fileName).stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            return fileName;
        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_UPLOAD_ERROR);
        }
    }

    public InputStream downloadFile(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucket).object(fileName).build());
        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_DOWNLOAD_ERROR);
        }
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_DELETE_ERROR);
        }
    }

    public List<String> listFiles() {
        try {
            List<String> fileNames = new ArrayList<>();
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucket).build());
            for (Result<Item> result : results) {
                fileNames.add(result.get().objectName());
            }
            return fileNames;
        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_LIST_ERROR);
        }
    }

    public boolean fileExists(String fileName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucket).object(fileName).build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public StatObjectResponse getFileMetadata(String fileName) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucket).object(fileName).build());
        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_METADATA_ERROR);
        }
    }

    public void copyFile(String sourceFileName, String targetFileName) {
        try {
            minioClient.copyObject(CopyObjectArgs.builder()
                    .source(CopySource.builder()
                            .bucket(bucket)
                            .object(sourceFileName)
                            .build())
                    .bucket(bucket)
                    .object(targetFileName)
                    .build());
        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_COPY_ERROR);
        }
    }

    public void moveFile(String sourceFileName, String targetFileName) {
        try {
            copyFile(sourceFileName, targetFileName);
        } catch (AppException e) {
            throw new AppException(ErrorCode.MINIO_MOVE_COPY_ERROR);
        }
        try {
            deleteFile(sourceFileName);
        } catch (AppException e) {
            throw new AppException(ErrorCode.MINIO_MOVE_DELETE_ERROR);
        }
    }
}
