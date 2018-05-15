package com.icarusrises.caseyellowanalysis.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
@EnableDynamoDBRepositories(basePackages = "com.icarusrises.caseyellowanalysis.persistence")
public class DynamoDBConfiguration {

    @Value("${dynamo.endpoint}")
    private String dynamoServiceEndpoint;

    @Value("${dynamo.region}")
    private String dynamoServiceRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB(ConfigurationManager configurationManager) {
        AWSCredentials credentials =
                new BasicAWSCredentials(configurationManager.awsAccessKeyID(),
                                        configurationManager.awsSecretAccessKey());

        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(dynamoServiceEndpoint, dynamoServiceRegion);

        AmazonDynamoDB amazonDynamoDB =
                AmazonDynamoDBClient.builder()
                                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                    .withEndpointConfiguration(endpointConfiguration)
                                    .build();

        return amazonDynamoDB;
    }
}
