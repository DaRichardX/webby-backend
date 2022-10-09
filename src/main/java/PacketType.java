public enum PacketType {
    HELLO, UNKNOWN
    , CLIENT_AUTH, SERVER_AUTH_ACCEPTED, SERVER_AUTH_REFUSED, EXPIRED_TOKEN
    , CLIENT_REQUEST, SERVER_RESPOND
    , SERVER_REQUEST, CLIENT_RESPOND
    , ACCESS_LEVEL_CHANGE, ACCESS_LEVEL_CHANGE_ACCEPT, ACCESS_LEVEL_CHANGE_REFUSED
    ;


    public static PacketType getValue(String s){
        switch (s){
            case "HELLO":
                return HELLO;
        }

        return UNKNOWN;
    }
}
