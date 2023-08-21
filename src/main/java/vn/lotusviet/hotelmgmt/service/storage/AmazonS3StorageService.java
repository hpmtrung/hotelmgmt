package vn.lotusviet.hotelmgmt.service.storage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties.Cloud.Aws.S3ClientConfig;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.util.FileUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AmazonS3StorageService implements StorageService {

  private final AmazonS3 s3Client;
  private final S3ClientConfig s3ClientConfig;

  public AmazonS3StorageService(AmazonS3 s3Client, ApplicationProperties applicationProperties) {
    this.s3Client = s3Client;
    this.s3ClientConfig = applicationProperties.getCloud().getAws().getS3();
  }

  @Override
  @LogAround
  public String saveFile(final MultipartFile multipartFile, final FileEntry entry) {
    Objects.requireNonNull(multipartFile);
    Objects.requireNonNull(entry);

    String key = resolveFileKey(entry);

    try {
      FileUtil.doTaskWithTemporaryFileFromMultipart(
          multipartFile,
          file -> {
            PutObjectRequest objectRequest =
                new PutObjectRequest(s3ClientConfig.getBucketName(), key, file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            objectRequest.setMetadata(metadata);

            s3Client.putObject(objectRequest);
          });

      return key;
    } catch (AmazonServiceException | IOException ex) {
      throw new StorageServiceException("Upload file unsuccessfully: " + ex.getMessage());
    }
  }

  @Override
  @LogAround
  public void deleteFiles(Collection<String> keys) {
    try {
      DeleteObjectsRequest dor =
          new DeleteObjectsRequest(s3ClientConfig.getBucketName())
              .withKeys(keys.stream().map(KeyVersion::new).collect(Collectors.toList()))
              .withQuiet(true);
      s3Client.deleteObjects(dor);
    } catch (AmazonServiceException ex) {
      throw new StorageServiceException("Delete file unsuccessfully: " + ex.getMessage());
    }
  }

  @Override
  public String getFileURL(final @NotNull String key) {
    Objects.requireNonNull(key);
    return s3ClientConfig.getBaseUrl() + key;
  }

  private String resolveFileKey(FileEntry entry) {
    return entry.getBrand().name().toLowerCase()
        + "/"
        + entry.getCategory().name().toLowerCase()
        + "/"
        + entry.getFilename();
  }

  private static class StorageServiceException extends RuntimeException {

    private static final long serialVersionUID = -998495811263862512L;

    public StorageServiceException(String message) {
      super(message);
    }

    public StorageServiceException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}