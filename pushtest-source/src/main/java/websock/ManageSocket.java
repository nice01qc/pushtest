package websock;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import util.RedisTool;
import websock.IndexSocket;

@ServerEndpoint("/manage")
public class ManageSocket {
    private static Logger logger = (Logger)LogManager.getLogger("other");
    private static CopyOnWriteArraySet<ManageSocket> manageSocketSet = new CopyOnWriteArraySet();
    private Session session;

    public ManageSocket() {
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        manageSocketSet.add(this);
        updateAllRoom();
    }

    @OnClose
    public void onClose() {
        manageSocketSet.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        String room = message.replaceAll(" ", "");
        if (room.matches("[:a-zA-Z0-9]+")) {
            if (room.equals(0)) {
                IndexSocket.clearAllRoom();
            }

            if (room.matches("^room:[0-9a-zA-Z]+$")) {
                IndexSocket.clearOneRoom(room.replaceAll("room:", ""));
            }

            if (room.equals("clear")) {
                RedisTool.emptyRedis();
            }
        }

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        logger.error(error.getMessage());
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendMessageByOut(String message) {
        Iterator var1 = manageSocketSet.iterator();

        while(var1.hasNext()) {
            ManageSocket item = (ManageSocket)var1.next();

            try {
                item.sendMessage(message);
            } catch (IOException var4) {
                var4.printStackTrace();
                logger.error(var4.getMessage());
            }
        }

    }

    public static void updateroom(String message) {
        sendMessageByOut("room:" + message);
    }

    public static void updateAllRoom() {
        if (RedisTool.isExit("clientRoom")) {
            Set<String> stringSet = RedisTool.getAllSetValue("clientRoom");
            Iterator var1 = stringSet.iterator();

            while(var1.hasNext()) {
                String string = (String)var1.next();
                sendMessageByOut("room:" + string);
            }
        }

    }
}
