import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PacketFactory {



    public static JsonObject createServerAuthAccepted(String token, AccessLevel level){
        JsonObject obj = new JsonObject();
        obj.add(JsonValues.JSON_TYPE_IDENTIFIER, JsonParser.parseString(PacketType.SERVER_AUTH_ACCEPTED.toString()));
        obj.add(JsonValues.TOKEN, JsonParser.parseString(token));
        obj.add(JsonValues., JsonParser.parseString(token));
        return obj;
    }

    public static JsonObject createServerAuthRefused(){
        JsonObject obj = new JsonObject();
        obj.add(JsonValues.JSON_TYPE_IDENTIFIER, JsonParser.parseString(PacketType.SERVER_AUTH_REFUSED.toString()));
        return obj;
    }

    public static JsonObject createServerExpiredToken(){
        JsonObject obj = new JsonObject();
        obj.add(JsonValues.JSON_TYPE_IDENTIFIER, JsonParser.parseString(PacketType.EXPIRED_TOKEN.toString()));
        return obj;
    }

}
