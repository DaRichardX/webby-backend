import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class SocketServer extends WebSocketServer {

    public static Logger logger = Logger.getLogger("Socket");

    public static HashMap<String, UserSession> sessions = new HashMap<>();

    public static HashMap<WebSocket, String> websocketToSession = new HashMap<>();

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
        if(websocketToSession.containsKey(webSocket)){
            sessions.get(websocketToSession.get(webSocket)).activeSockets.remove(webSocket);
            if(sessions.get(websocketToSession.get(webSocket)).isExpired()){
                sessions.remove(websocketToSession.get(webSocket));
            }
            websocketToSession.remove(webSocket);
        }


    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        if(JsonParser.parseString(message).getAsJsonObject().has(JsonValues.JSON_TYPE_IDENTIFIER)){
            logger.info(webSocket.getRemoteSocketAddress() + ": " + JsonParser.parseString(message).getAsJsonObject().get(JsonValues.JSON_TYPE_IDENTIFIER).getAsString());
            handelMessage(webSocket, message);
        }else{
            logger.warn(webSocket.getRemoteSocketAddress() + ": INVALID PACKET");
        }

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







    public void handelMessage(WebSocket webSocket, String message){
        JsonObject msg = JsonParser.parseString(message).getAsJsonObject();
        PacketType type = PacketType.getValue(msg.get(JsonValues.JSON_TYPE_IDENTIFIER).getAsString());
        if(type == PacketType.HELLO){
            if(!helloHandler(msg)){
                webSocket.send(PacketFactory.createServerExpiredToken().getAsString());
                logger.info("Expired Token -> " + webSocket.getRemoteSocketAddress());
            }else{
                webSocket.send(PacketFactory.createServerAuthAccepted(msg.get(JsonValues.TOKEN).toString(), sessions.get(JsonValues.TOKEN).level).getAsString());
                logger.info("Auth Accepted -> " + webSocket.getRemoteSocketAddress());
            }

        }else if(type == PacketType.CLIENT_AUTH){
            String token = clientAuthHandler(msg);
            if(clientAuthHandler(msg) != null){
                webSocket.send(PacketFactory.createServerAuthAccepted(token, sessions.get(token).level).getAsString());
                logger.info("Auth Accepted -> " + webSocket.getRemoteSocketAddress());
                sessions.get(token).activeSockets.add(webSocket);
                websocketToSession.put(webSocket, token);
            }else{
                webSocket.send(PacketFactory.createServerAuthRefused().getAsString());
                logger.info("Auth Refused -> " + webSocket.getRemoteSocketAddress());
            }
        }
    }


    public String clientAuthHandler(JsonObject obj){
        String username = obj.get(JsonValues.USERNAME).getAsString();
        String password = obj.get(JsonValues.PASSWORD).getAsString();

        AccessLevel level = Database.login(username, password);

        if(level == AccessLevel.INVALID){
            return null;
        }

        String key = Database.createNewKey();
        UserSession session = new UserSession(key, username, password);
        session.level = level;
        sessions.put(key, session);

        return key;
    }

    public boolean helloHandler(JsonObject obj){
        if(obj.get("hasToken").getAsBoolean()){
            if(sessions.containsKey(obj.get(JsonValues.TOKEN).getAsString())){
                return !sessions.get(obj.get(JsonValues.TOKEN).getAsString()).isExpired();
            }else{
                return false;
            }
        }
        return false;
    }
}
