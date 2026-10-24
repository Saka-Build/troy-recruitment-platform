package com.troy.ats.service.impl;


import com.troy.ats.exception.FileStorageException;
import com.troy.ats.service.FileStorageService;
import com.troy.ats.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service("fileStorageService")
@Profile("prod")
@RequiredArgsConstructor
@Slf4j
public class S3FileStorageServiceImpl implements FileStorageService {

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.upload-original-cv-dir:upload/originalcv}")
    private String uploadOriginalCVDir;

    @Value("${aws.s3.upload-troy-cv-dir:upload/troycv}")
    private String uploadTroyCVDir;

    @Value("${aws.s3.upload-photo-dir:upload/employee/photo}")
    private String uploadPhotoDir;

    @Value("${aws.s3.presign-ttl-minutes:10}")
    private long presignTtlMinutes;


    /**
     * Resolve per call rather than held on the singleton, so concurrent
     * uploads of different kinds cannot overwrite each other's prefix.
     */
    private String getUploadPrefix(boolean isOriginalCV, boolean isPhoto) {

        return isPhoto ? uploadPhotoDir : isOriginalCV ? uploadOriginalCVDir : uploadTroyCVDir;
    }

    /** Readable label for the log lines, so a bare key is never ambiguous. */
    private String getFileType(boolean isOriginalCV, boolean isPhoto) {

        return isPhoto ? "PHOTO" : isOriginalCV ? "ORIGINAL_CV" : "TROY_CV";
    }

    /**
     *
     * @param file
     * @param id
     * @return
     */
    @Override
    public String store(MultipartFile file, UUID id, boolean isOriginalCV, boolean isPhoto) {

        String fileType = getFileType(isOriginalCV, isPhoto);

        if (file == null || file.isEmpty()) {
            log.warn("S3 PUT rejected - empty file. type={} entityId={}", fileType, id);
            throw new IllegalArgumentException("CV file cannot be empty");
        }

        String extension = CommonUtil.getExtension(file.getOriginalFilename());
        CommonUtil.validateExtension(extension);

        String key = getUploadPrefix(isOriginalCV, isPhoto) + "/" + id + extension;

        log.info("S3 PUT start - type={} entityId={} bucket={} key={} originalName={} contentType={} size={}B",
                fileType, id, bucketName, key, file.getOriginalFilename(), file.getContentType(), file.getSize());

        long start = System.nanoTime();

        try (InputStream inputStream = file.getInputStream()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            PutObjectResponse response =
                    s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            log.info("S3 PUT ok - type={} bucket={} key={} size={}B eTag={} requestId={} in {}ms",
                    fileType, bucketName, key, file.getSize(), response.eTag(),
                    response.responseMetadata().requestId(), elapsedMs(start));

            return key;

        } catch (S3Exception e) {

            log.error("S3 PUT failed - type={} bucket={} key={} status={} awsErrorCode={} awsMessage={} requestId={} extendedRequestId={} in {}ms",
                    fileType, bucketName, key, e.statusCode(), awsErrorCode(e), awsErrorMessage(e),
                    e.requestId(), e.extendedRequestId(), elapsedMs(start), e);

            throw new FileStorageException("Failed to store file in S3: " + key, e);

        } catch (SdkException e) {

            // Connectivity, credential resolution or region misconfiguration - never reached S3.
            log.error("S3 PUT failed before reaching S3 - type={} bucket={} key={} reason={} in {}ms",
                    fileType, bucketName, key, e.getMessage(), elapsedMs(start), e);

            throw new FileStorageException("Failed to store file in S3: " + key, e);

        } catch (IOException e) {

            log.error("S3 PUT failed - could not read upload stream. type={} key={} originalName={} in {}ms",
                    fileType, key, file.getOriginalFilename(), elapsedMs(start), e);

            throw new FileStorageException("Failed to store file in S3: " + key, e);
        }
    }

    /**
     *
     * @param fileUrl
     * @param isOriginalCV
     * @param isPhoto
     */
    @Override
    public void delete(String fileUrl, boolean isOriginalCV, boolean isPhoto) {

        String fileType = getFileType(isOriginalCV, isPhoto);

        if (fileUrl == null || fileUrl.isBlank()) {
            log.debug("S3 DELETE skipped - no key held for type={}", fileType);
            return;
        }

        log.info("S3 DELETE start - type={} bucket={} key={}", fileType, bucketName, fileUrl);

        long start = System.nanoTime();

        try {

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileUrl)
                    .build();

            DeleteObjectResponse response = s3Client.deleteObject(deleteObjectRequest);

            log.info("S3 Delete ok - type={} bucket={} key={} requestId={} in {}ms",
                    fileType, bucketName, fileUrl, response.responseMetadata().requestId(), elapsedMs(start));

        } catch (S3Exception e) {

            log.warn("S3 DELETE failed - type={} bucket={} key={} status={} awsErrorCode={} requestId={} in {}ms",
                    fileType, bucketName, fileUrl, e.statusCode(), awsErrorCode(e), e.requestId(), elapsedMs(start), e);

        } catch (SdkException e) {

            log.warn("S3 DELETE failed before reaching S3 - type={} bucket={} key={} reason={} in {}ms",
                    fileType, bucketName, fileUrl, e.getMessage(), elapsedMs(start), e);
        }
    }

    /**
     *
     * @param key
     * @param downloadFileName
     * @return
     */
    @Override
    public URL presignedUrl(String key, String downloadFileName) {

        long start = System.nanoTime();

        try {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .responseContentDisposition("attachment; filename=\"" + downloadFileName + "\"")
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(presignTtlMinutes))
                    .getObjectRequest(getObjectRequest)
                    .build();

            URL url = s3Presigner.presignGetObject(presignRequest).url();
            log.info("S3 PRESIGN ok - bucket={} key={} fileName={} ttlMinutes={} in {}ms",
                    bucketName, key, downloadFileName, presignTtlMinutes, elapsedMs(start));

            return url;

        } catch (SdkException e) {

            log.error("S3 PRESIGN failed - bucket={} key={} reason={} in {}ms",
                    bucketName, key, e.getMessage(), elapsedMs(start), e);

            throw new FileStorageException("Failed to generate download URL for: " + key, e);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public long getPresignTtlMinutes() {
        return presignTtlMinutes;
    }

    private long elapsedMs(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }

    private String awsErrorCode(AwsServiceException e) {
        return e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "unknown";
    }

    private String awsErrorMessage(AwsServiceException e) {
        return e.awsErrorDetails() != null ? e.awsErrorDetails().errorMessage() : e.getMessage();
    }
}
