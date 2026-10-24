package com.troy.ats.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@Profile("prod")
public class S3Config {

    @Value("${aws.region}")
    private String region;

    /**
     * Overrides the AWS endpoint so the app can talk to a local S3-compatible server
     * (MinIO, LocalStack) during development. Left blank in every real environment,
     * where the SDK resolves the regional AWS endpoint itself.
     */
    @Value("${aws.s3.endpoint:}")
    private String endpoint;

    /**
     IAM Role wioh policy attched to it
     */
    @Bean
    public S3Client s3Client() {

        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create());

        applyEndpointOverride(builder);

        return builder.build();
    }

    @Bean
    public S3Presigner s3Presigner() {

        S3Presigner.Builder builder = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create());

        if (StringUtils.isNotBlank(endpoint)) {
            builder.endpointOverride(URI.create(endpoint))
                    .serviceConfiguration(S3Configuration.builder()
                            .pathStyleAccessEnabled(true)
                            .build());
        }

        return builder.build();
    }

    /**
     * Local S3 servers are addressed as host/bucket/key rather than bucket.host/key,
     * so path-style has to be forced alongside the endpoint.
     */
    private void applyEndpointOverride(S3ClientBuilder builder) {

        if (StringUtils.isBlank(endpoint)) {
            return;
        }

        builder.endpointOverride(URI.create(endpoint))
                .forcePathStyle(true);
    }
}
