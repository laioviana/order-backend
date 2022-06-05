package com.peecho.orderbackend.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileReadUtils {

    private FileReadUtils() {

    }

    public static String readFile(String fileName) throws IOException, URISyntaxException {
        return Files.readString(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));
    }

}