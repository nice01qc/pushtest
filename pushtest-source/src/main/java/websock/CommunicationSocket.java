package websock;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

@ServerEndpoint("/communication")
public class CommunicationSocket {
    private static Logger logger = (Logger)LogManager.getLogger("other");
    private String room = "";
    private static CopyOnWriteArraySet<CommunicationSocket> resultSocketSet = new CopyOnWriteArraySet();
    private Session session;

    public CommunicationSocket() {
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        resultSocketSet.add(this);
    }

    @OnClose
    public void onClose() {
        resultSocketSet.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        this.room = message;
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        logger.error(error.getMessage());
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendMessageByOut(String room, String message) {
        Iterator var2 = resultSocketSet.iterator();

        while(var2.hasNext()) {
            CommunicationSocket item = (CommunicationSocket)var2.next();
            if (item.room.equals(room)) {
                try {
                    item.sendMessage(message);
                } catch (IOException var5) {
                    var5.printStackTrace();
                    logger.error(var5.getMessage());
                }
            }
        }

    }
}
