
import org.java_websocket.WebSocket;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class UserSession {

    public String sessionKey;

    public String sessionUsername;

    public String sessionPassword;

    public Date sessionStart;

    public Date lastAction;

    public AccessLevel level;

    public HashSet<WebSocket> activeSockets = new HashSet<>();

    public static final int MAX_NO_ACTION_TIMEOUT_SECONDS = 300; //5 minutes

    public UserSession(String key, String username, String password){
        sessionStart = Calendar.getInstance().getTime();
        lastAction = Calendar.getInstance().getTime();
        sessionPassword = password;
        sessionUsername = username;
        sessionKey = key;
    }

    public void updateAction(){
        lastAction = Calendar.getInstance().getTime();
    }

    public boolean isExpired(){
        if ((Calendar.getInstance().getTime().getTime() - lastAction.getTime()) / 1000 > MAX_NO_ACTION_TIMEOUT_SECONDS){
            return true;
        }

        return activeSockets.isEmpty();
    }

}
