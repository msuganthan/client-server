package clientserver.server;

import clientserver.ConfigLoader;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new RuntimeException("Usage: java Server <config-file-path>");
        }

        Properties config = ConfigLoader.loadConfig(args[0]);

        String directoryToWrite = config.getProperty("directory.write");
        int port = Integer.parseInt(config.getProperty("server.port"));

        Path pathToWrite = Paths.get(directoryToWrite);
        if (!Files.exists(pathToWrite)) {
            Files.createDirectories(pathToWrite);
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server listening on port " + port);

            while (true) {
                new ClientHandler(serverSocket.accept(), directoryToWrite).start();
            }
        }
    }
}
