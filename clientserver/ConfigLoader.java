package clientserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    public static Properties loadConfig(String configFilePath) throws IOException {
        Properties config = new Properties();
        InputStream inputStream = new FileInputStream(configFilePath);
        config.load(inputStream);
        return config;
    }
}
