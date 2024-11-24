package clientserver.client;

import clientserver.ConfigLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Pattern;

public class DirectoryMonitorClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 1) {
            throw new RuntimeException("Usage: java DirectoryMonitorClient <config.file-path>");
        }

        Properties config = ConfigLoader.loadConfig(args[0]);
        Path directoryToWatch = Paths.get(config.getProperty("directory.path"));
        String keyRegex = config.getProperty("key.filter.regex");
        String serverAddress = config.getProperty("server.address");
        int serverPort = Integer.parseInt(config.getProperty("server.port"));

        if (!Files.exists(directoryToWatch)) {
            Files.createDirectories(directoryToWatch);
        }

        if (keyRegex == null || keyRegex.isEmpty()) {
            throw new RuntimeException("Key filter regex required");
        }

        if (serverAddress == null || serverAddress.isEmpty()) {
            throw new RuntimeException("Server address required");
        }

        Pattern keyPattern = Pattern.compile(keyRegex);

        DirectoryMonitor client = new DirectoryMonitor(directoryToWatch, keyPattern, serverAddress, serverPort);
        client.monitor();
    }
}
