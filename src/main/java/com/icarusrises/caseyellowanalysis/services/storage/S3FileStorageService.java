package com.icarusrises.caseyellowanalysis.services.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.icarusrises.caseyellowanalysis.configuration.ConfigurationManager;
import com.icarusrises.caseyellowanalysis.exceptions.IORuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@Profile("prod")
public class S3FileStorageService implements StorageService {

    @Value("${bucket_name}")
    private String bucketName;

    private AmazonS3 s3Client;
    private ConfigurationManager configurationManager;

    @Autowired
    public S3FileStorageService(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @PostConstruct
    public void init() throws IOException {
            AWSCredentials credentials = new BasicAWSCredentials(configurationManager.awsAccessKeyID(),
                                                                 configurationManager.awsSecretAccessKey());
            s3Client = AmazonS3ClientBuilder.standard()
                                            .withRegion(Regions.EU_CENTRAL_1)
                                            .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                            .build();
    }

    @Override
    public File getFile(String objectKey) {
        log.info("Fetch file from s3: " + objectKey);

        if (!isObjectExist(objectKey)) {
            throw new IORuntimeException(String.format("Failed to get file from S3, file :%s not exist", objectKey));
        }

        File newFile = new File(System.getProperty("java.io.tmpdir"), objectKey.substring(objectKey.lastIndexOf("_") +1));
        S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, objectKey));

        try (InputStream objectData = object.getObjectContent()) {
            FileUtils.copyInputStreamToFile(objectData, newFile);

        } catch (IOException e) {
            log.error("Failed to get file from S3, " + e.getMessage(), e);
            throw new IORuntimeException("Failed to get file from S3, " + e.getMessage(), e);
        }

        return newFile;
    }


    private boolean isObjectExist(String path) {
        return s3Client.doesObjectExist(bucketName, path);
    }

}
