package clientserver.client;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.logging.Logger;

public class DirectoryMonitor {

    private static final Logger logger = Logger.getLogger(DirectoryMonitor.class.getName());

    private final Path directoryToWatch;
    private final Pattern keyPattern;
    private final String serverAdress;
    private final int port;

    public DirectoryMonitor(Path directoryToWatch, Pattern keyPattern, String serverAdress, int port) {
        this.directoryToWatch = directoryToWatch;
        this.keyPattern = keyPattern;
        this.serverAdress = serverAdress;
        this.port = port;
    }

    @SuppressWarnings("unchecked")
    public void monitor() throws IOException, InterruptedException {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            directoryToWatch.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                        Path filePath = watchEvent.context();

                        if (filePath.toString().endsWith(".properties")) {
                            logger.info("New properties file detected: " + filePath);
                            FileProcessor fileProcessor = new FileProcessor(keyPattern);
                            Map<String, String> filteredProperties = fileProcessor.readPropertiesFilesWithFilter(directoryToWatch.resolve(filePath));

                            if (filteredProperties != null && !filteredProperties.isEmpty()) {
                                ServerCommunicator serverCommunicator = new ServerCommunicator(serverAdress, port);
                                serverCommunicator.sendToServer(filteredProperties);
                                Files.delete(directoryToWatch.resolve(filePath));
                                logger.info("File processed and deleted: " + filePath);
                            } else {
                                logger.info("No matching keys found in the file. Skipping: " + filePath);
                            }
                        }
                    }
                }
                key.reset();
            }

        }
    }
}
