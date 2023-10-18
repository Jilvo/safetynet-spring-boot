package com.safetynet.alerts.services;

import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class JsonFileService {
    private static final String JSON_FILE_PATH = "src/main/java/data.json";

    private final Cache<String, String> jsonCache = Caffeine.newBuilder().maximumSize(1).build();
    public String readJsonFile() throws IOException {
        String cachedJson = jsonCache.getIfPresent("cachedJson");
        if (cachedJson != null) {
            return cachedJson;
        } else {
            String json = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
            jsonCache.put("cachedJson", json);
            return json;
        }
    }
    public Boolean updateJsonFile(String updatedJson) {
        jsonCache.put("cachedJson", updatedJson);
        return true;
    }

}
