import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class SocketServer extends WebSocketServer {

    public static Logger logger = Logger.getLogger("Socket");

    public SocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("New Connection: " + webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        logger.warn(webSocket.getRemoteSocketAddress() + " Closed with reason: " + reason);
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        logger.info(webSocket.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.warn("Error with " + webSocket.getRemoteSocketAddress());
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        logger.info("WebSocket server started");
    }
}
