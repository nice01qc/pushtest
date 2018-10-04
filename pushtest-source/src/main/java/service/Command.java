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
import websock.IndexSocket;

@WebServlet(
        name = "Command",
        urlPatterns = {"/command"}
)
public class Command extends HttpServlet {
    private static Logger logger = (Logger)LogManager.getLogger("other");

    public Command() {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IndexSocketMessage ism = new IndexSocketMessage();
        request.setCharacterEncoding("utf-8");
        String command = request.getParameter("command");
        String room = request.getParameter("room");
        ism.setRoom(room).setCommand(command).setDirection(DirecEnum.COMMAND);
        if ("deleteAll".equals(command) && !room.equals("")) {
            IndexSocket.sendMessageByOut(ism);
            IndexSocket.clearOneRoomMessage(room);
        }

    }
}
