package clientserver.server;

import clientserver.client.ServerCommunicator;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final Socket clientSocket;
    private final String directoryToWrite;

    public ClientHandler(Socket clientSocket, String directoryToWrite) {
        this.clientSocket = clientSocket;
        this.directoryToWrite = directoryToWrite;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String line;
            Properties properties = new Properties();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    properties.setProperty(parts[0], parts[1]);
                }
            }

            File file = new File(directoryToWrite, "filtered_" + System.currentTimeMillis() + ".properties");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String key : properties.stringPropertyNames()) {
                    String value = properties.getProperty(key);
                    writer.write(key + "=" + value);
                    writer.newLine();
                }
            }

            logger.info("Received and save properties to: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.info("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
