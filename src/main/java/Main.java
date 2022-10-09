import org.apache.log4j.Logger;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;

public class Main {

    public static final String HOST_ADDRESS = "localhost";

    public static final int PORT = 8488;

    public static final Logger logger = Logger.getLogger("Server");

    public static void main(String[] args) {
        logger.info("Starting server on " + HOST_ADDRESS + ":" + PORT + "...");
        WebSocketServer sev = new SocketServer(new InetSocketAddress(HOST_ADDRESS, PORT));
        sev.start();

    }


}
