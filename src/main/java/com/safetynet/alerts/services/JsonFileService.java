package com.safetynet.alerts.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileService {
    public String readJsonFile() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/main/java/data.json")));
        return content;
    }

    public void writeJsonFile(String content) throws IOException {
        Files.write(Paths.get("src/main/java/data.json"), content.getBytes());
    }
}