package com.icarusrises.caseyellowanalysis.commons;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.Objects.nonNull;

public interface Utils {

    Logger log = Logger.getLogger(Utils.class);

    static void deleteFile(File file) {
        try {
            if (nonNull(file) && file.exists()) {
                Files.deleteIfExists(file.toPath());
            }
        } catch (IOException e) {
            log.error(String.format("Failed to delete file: %s", e.getMessage()));
        }
    }
}
