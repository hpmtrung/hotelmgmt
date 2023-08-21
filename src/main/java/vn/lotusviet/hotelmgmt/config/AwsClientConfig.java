package vn.lotusviet.hotelmgmt.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AwsClientConfig {

  private final ApplicationProperties applicationProperties;

  public AwsClientConfig(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  public AmazonS3 s3Client() {
    ApplicationProperties.Cloud.Aws awsProperties = applicationProperties.getCloud().getAws();
    AWSCredentials credentials =
        new BasicAWSCredentials(
            awsProperties.getCredentials().getAccessKey(),
            awsProperties.getCredentials().getSecretKey());
    return AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.fromName(awsProperties.getS3().getRegion()))
        .build();
  }
}