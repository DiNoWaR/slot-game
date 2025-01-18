package org.config.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.config.model.GameConfig;

import java.io.File;
import java.io.IOException;

public class Parser {
    public static GameConfig parseConfig(String filePath) throws IOException {
        var objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), GameConfig.class);
    }
}
