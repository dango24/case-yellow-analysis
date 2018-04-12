package com.icarusrises.caseyellowanalysis.configuration;

import com.icarusrises.caseyellowanalysis.exceptions.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
@Profile("prod")
public class ConfigurationManagerImpl implements ConfigurationManager {

    @Value("${credentials_path}")
    private String credentialsPath;

    @Value("${encryption_key}")
    private String encryptionKey;

    private String accessKeyID;
    private String secretAccessKey;
    private String googleVisionKey;

    @Override
    public String awsAccessKeyID() {
        return accessKeyID;
    }

    @Override
    public String awsSecretAccessKey() {
        return secretAccessKey;
    }

    @Override
    public String googleVisionKey() {
        return googleVisionKey;
    }

    @PostConstruct
    private boolean buildCredentialsFromEncryptedCredentials() throws IOException {
        try {
            String key = encryptSHA512(encryptionKey);
            String decryptedCredentials = decryptCredentials(key);

            parseDecryptedCredentials(decryptedCredentials);

            return true;

        } catch (ConfigurationException e) {
            log.error("Failed to build credentials, " + e.getMessage(), e);
            return false;
        }
    }

    private String encryptSHA512(String target) {
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-512");
            sh.update(target.getBytes());

            return String.format("%0128x", new BigInteger(1, sh.digest()));

        } catch (NoSuchAlgorithmException e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }

    private String decryptCredentials(String key) throws IOException {
        String[] commands = {credentialsPath, key};
        Process process = Runtime.getRuntime().exec(commands);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

        return stdInput.readLine();
    }

    private void parseDecryptedCredentials(String decryptedCredentials) {
        try {
            decryptedCredentials = decryptedCredentials.replaceAll("\\$", "");
            String[] credentials = decryptedCredentials.split(";");
            accessKeyID = credentials[0];
            secretAccessKey = credentials[1];
            googleVisionKey = credentials[2];

        } catch (Exception e) {
            throw new ConfigurationException("Failed to decrypted credentials: " + decryptedCredentials + ", cause: " + e.getMessage(), e);
        }
    }
}
