package clientserver.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Logger;

public class ServerCommunicator {

    private static final Logger logger = Logger.getLogger(ServerCommunicator.class.getName());

    private final String serverAddress;
    private final int serverPort;

    public ServerCommunicator(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void sendToServer(Map<String, String> filteredProperties) throws IOException {
        try (Socket socket = new Socket(serverAddress, serverPort);
             OutputStream out = socket.getOutputStream()) {
            for (Map.Entry<String, String> entry : filteredProperties.entrySet()) {
                String message = entry.getKey() + "=" + entry.getValue() + "\n";
                out.write(message.getBytes());
            }
            out.flush();
        }
        logger.info("Sent filtered data to server: " + serverAddress);
    }
}
