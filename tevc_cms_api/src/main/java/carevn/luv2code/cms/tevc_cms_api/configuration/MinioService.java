package carevn.luv2code.cms.tevc_cms_api.configuration;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
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
            throw new RuntimeException("Error initializing MinIO bucket", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(fileName).stream(
                            file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to MinIO", e);
        }
    }
}
