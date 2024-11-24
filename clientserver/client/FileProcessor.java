package clientserver.client;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileProcessor {

    private final Pattern keyPattern;

    public FileProcessor(Pattern keyPattern) {
        this.keyPattern = keyPattern;
    }

    public Map<String, String> readPropertiesFilesWithFilter(Path filePath) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(filePath.toFile()));

        return properties.stringPropertyNames()
                .stream()
                .filter(key -> keyPattern.matcher(key).matches())
                .collect(Collectors.toMap(
                        key -> key,
                        properties::getProperty
                ));
    }
}
