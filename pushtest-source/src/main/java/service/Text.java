package service;

import entity.DirecEnum;
import entity.IndexSocketMessage;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import util.RedisKey;
import util.RedisTool;
import websock.IndexSocket;

@WebServlet(name = "Text", urlPatterns = {"/text"})
public class Text extends HttpServlet {
    private static Logger logger = (Logger) LogManager.getLogger("other");

    public Text() {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        this.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        IndexSocketMessage ism = new IndexSocketMessage();
        String text = request.getParameter("text");
        String room = request.getParameter("room");
        String roomTextRedisKey = RedisKey.getTextRedisKeyByRoom(room);
        ism.setText(text).setRoom(room).setDirection(DirecEnum.TEXT);
        if (text != null && text.length() > 0) {
            RedisTool.listAddValueByKey(roomTextRedisKey, text);
            IndexSocket.sendMessageByOut(ism);
        }

    }
}