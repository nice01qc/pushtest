package service;

import entity.DirecEnum;
import entity.IndexSocketMessage;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import util.RedisKey;
import util.RedisTool;
import websock.IndexSocket;

@WebServlet(
        name = "Img",
        urlPatterns = {"/img"}
)
public class Img extends HttpServlet {
    private static Logger logger = (Logger)LogManager.getLogger("other");

    public Img() {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IndexSocketMessage ism = new IndexSocketMessage();
        request.setCharacterEncoding("utf-8");
        String imgdata = request.getParameter("img");
        String room = request.getParameter("room");
        String roomImgRedisKey = RedisKey.getImgRedisKeyByRoom(room);
        ism.setImg(imgdata).setRoom(room).setDirection(DirecEnum.IMG);
        if (imgdata != null && imgdata.length() > 200) {
            RedisTool.listAddValueByKey(roomImgRedisKey, imgdata);
            IndexSocket.sendMessageByOut(ism);
        }

    }
}
