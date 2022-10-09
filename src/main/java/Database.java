import java.util.HashMap;
import java.util.UUID;

public class Database {

    public static HashMap<String, String> userInfo = new HashMap<>();

    public static HashMap<String, AccessLevel> userLevel = new HashMap<>();

    public static AccessLevel login(String username, String pw){
        if(userInfo.containsKey(username)){
            if(pw.equals(userInfo.get(username))){
                return userLevel.get(username);
            }
        }

        return AccessLevel.INVALID;
    }

    public static String createNewKey(){
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        return uuid1.toString() + "-" + uuid2.toString();
    }


}
