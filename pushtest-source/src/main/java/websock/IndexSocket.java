package websock;

import com.google.gson.Gson;
import entity.Answer;
import entity.DirecEnum;
import entity.IndexSocketMessage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import util.RedisKey;
import util.RedisTool;

@ServerEndpoint("/indexSocket")
public class IndexSocket {
    private static Gson gson = new Gson();
    private static Logger logger = (Logger) LogManager.getLogger("other");
    private String room = "";
    private static Set<IndexSocket> webSocketSet = Collections.synchronizedSet(new HashSet(12));
    private static ConcurrentHashMap<String, Set<String>> statusHashMap = new ConcurrentHashMap(16);
    private static ConcurrentHashMap<String, HashMap<String, String>> answerHashMap = new ConcurrentHashMap<String, HashMap<String, String>>();

    private Session session;

    public IndexSocket() {
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        IndexSocketMessage ism = (IndexSocketMessage) gson.fromJson(message, IndexSocketMessage.class);
        DirecEnum diretion = ism.getDirection();

        if (diretion == DirecEnum.IMGSTATENO && !ism.imgSerialNumIsNull()) {
            sendMessageByOut(ism.setDirection(DirecEnum.IMGSTATEYES).setRoom(this.room));
            addToStatusHashMap(ism.getRoom(), ism.getImgSerialNum());
        } else if (diretion == DirecEnum.IMGSTATEYES && !ism.imgSerialNumIsNull()) {
            sendMessageByOut(ism.setDirection(DirecEnum.IMGSTATENO).setRoom(this.room));
            delFromStatusHashMap(ism.getRoom(), ism.getImgSerialNum());
        } else if (diretion == DirecEnum.ANSWER && !ism.answersIsNull()) {
            if (this.room == null || this.room.equals("")) return;
            ism.setRoom(this.room);
            Answer answer = ism.getAnswers();
            if (!answerHashMap.contains(this.room)) {
                answerHashMap.put(this.room, new HashMap<String, String>());
            }
            HashMap<String, String> hashMap = answerHashMap.get(this.room);
            hashMap.put(answer.getNum(), answer.getResult());
            sendMessageByOut(ism);
        } else if (diretion == DirecEnum.ROOM && !ism.roomIsNull()) {
            this.room = ism.getRoom();
            this.initAllMessage(ism);
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        logger.error(error.getMessage());
    }

    public synchronized void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException var3) {
            var3.printStackTrace();
            logger.error(var3.getMessage());
        }

    }

    private void initAllMessage(IndexSocketMessage ism) {
        String room = ism.getRoom();
        IndexSocketMessage result = new IndexSocketMessage();
        RedisTool.setAddValueByKey(RedisKey.getCLIENTROOM(), room);
        ManageSocket.updateroom(room);
        this.room = room;
        String roomTextRedisKey = RedisKey.getTextRedisKeyByRoom(room);
        List<String> roomTextList = null;
        int i;
        if (RedisTool.isExit(roomTextRedisKey)) {
            this.sendMessage(gson.toJson(result.clear().setDirection(DirecEnum.TEXTNUM).setTextNum(String.valueOf(RedisTool.getListLengthByKey(roomTextRedisKey)))));
            roomTextList = RedisTool.getAllByKey(roomTextRedisKey);
            if (roomTextList.size() > 500) {
                RedisTool.delKey(roomTextRedisKey);
            }

            if (roomTextList != null && !roomTextList.isEmpty()) {
                result.clear().setDirection(DirecEnum.TEXT).setTextNum((String) null);

                for (i = roomTextList.size() - 1; i >= 0; --i) {
                    this.sendMessage(gson.toJson(result.setText((String) roomTextList.get(i))));
                }
            }
        }

        String roomImgRedisKey = RedisKey.getImgRedisKeyByRoom(room);
        List<String> roomImgList = null;
        if (RedisTool.isExit(roomImgRedisKey)) {
            this.sendMessage(gson.toJson(result.clear().setDirection(DirecEnum.IMGNUM).setImgNum(String.valueOf(RedisTool.getListLengthByKey(roomImgRedisKey)))));
            roomImgList = RedisTool.getAllByKey(roomImgRedisKey);
            if (roomImgList.size() > 250) {
                RedisTool.delKey(roomImgRedisKey);
            }

            if (roomImgList != null && !roomImgList.isEmpty()) {
                result.clear().setDirection(DirecEnum.IMG);

                for (i = roomImgList.size() - 1; i >= 0; --i) {
                    this.sendMessage(gson.toJson(result.setImg((String) roomImgList.get(i))));
                }
            }
        }

        if (statusHashMap.containsKey(this.room)) {
            Set<String> stringSet = (Set) statusHashMap.get(room);
            this.sendMessage(gson.toJson(result.clear().setDirection(DirecEnum.ALLIMAGESTATE).setAllImageState(stringSet)));
        }
        if (answerHashMap.containsKey(this.room)) {
            HashMap<String, String> hashMap = answerHashMap.get(this.room);
            IndexSocketMessage idx = new IndexSocketMessage();
            idx.setDirection(DirecEnum.ANSWER);
            for (String x : hashMap.keySet()) {
                Answer answer = new Answer(x, hashMap.get(x));
                idx.setAnswers(answer);
                this.sendMessage(gson.toJson(idx));
            }
        }

    }

    public static void sendMessageByOut(IndexSocketMessage ism) {
        Iterator allSockets = webSocketSet.iterator();

        while (allSockets.hasNext()) {
            IndexSocket socket = (IndexSocket) allSockets.next();
            if (socket.room != null && socket.room.equals(ism.getRoom())) {
                socket.sendMessage(gson.toJson(ism));
            }
        }

    }

    public static void clearAllRoom() throws IOException {
        Iterator var0 = webSocketSet.iterator();

        while (var0.hasNext()) {
            IndexSocket indexSocket = (IndexSocket) var0.next();
            indexSocket.session.close();
        }

        webSocketSet.clear();
        statusHashMap.clear();
        RedisTool.delKey(RedisKey.getCLIENTROOM());
    }

    public static void clearOneRoom(String room) throws IOException {
        Iterator var1 = webSocketSet.iterator();

        while (var1.hasNext()) {
            IndexSocket indexSocket = (IndexSocket) var1.next();
            if (indexSocket.room != null && indexSocket.room.equals(room)) {
                webSocketSet.remove(indexSocket);
                indexSocket.session.close();
                break;
            }
        }

        if (RedisTool.isExit(RedisKey.getCLIENTROOM())) {
            RedisTool.delSetData(RedisKey.getCLIENTROOM(), room);
        }

        if (RedisTool.isExit(RedisKey.getTextRedisKeyByRoom(room))) {
            RedisTool.delKey(RedisKey.getTextRedisKeyByRoom(room));
        }

        if (RedisTool.isExit(RedisKey.getImgRedisKeyByRoom(room))) {
            RedisTool.delKey(RedisKey.getImgRedisKeyByRoom(room));
        }

        statusHashMap.remove(room);
        answerHashMap.remove(room);
    }

    public static void clearOneRoomMessage(String room) throws IOException {
        if (RedisTool.isExit(RedisKey.getTextRedisKeyByRoom(room))) {
            RedisTool.delKey(RedisKey.getTextRedisKeyByRoom(room));
        }

        if (RedisTool.isExit(RedisKey.getImgRedisKeyByRoom(room))) {
            RedisTool.delKey(RedisKey.getImgRedisKeyByRoom(room));
        }

        statusHashMap.clear();
        answerHashMap.clear();
    }

    private static void addToStatusHashMap(String room, String message) {
        if (!"".equals(room)) {
            if (statusHashMap.containsKey(room)) {
                ((Set) statusHashMap.get(room)).add(message);
            } else {
                Set<String> stringSet = new HashSet();
                stringSet.add(message);
                statusHashMap.put(room, stringSet);
            }
        }

    }

    private static void delFromStatusHashMap(String room, String message) {
        if (!"".equals(room) && statusHashMap.containsKey(room) && ((Set) statusHashMap.get(room)).contains(message)) {
            ((Set) statusHashMap.get(room)).remove(message);
        }

    }
}
