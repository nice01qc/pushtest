package util;

public class RedisKey {
    private static final String CLIENTROOM = "clientRoom";
    private static final String TEXTPOSTFIX = "text";
    private static final String IMGPOSTFIX = "img";

    public RedisKey() {
    }

    public static String getCLIENTROOM() {
        return "clientRoom";
    }

    public static String getTextRedisKeyByRoom(String room) {
        return room + "text";
    }

    public static String getImgRedisKeyByRoom(String room) {
        return room + "img";
    }
}
