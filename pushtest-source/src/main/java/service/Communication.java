package service;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import websock.CommunicationSocket;

@WebServlet(
        name = "Communication",
        urlPatterns = {"/communication"}
)
public class Communication extends HttpServlet {
    public Communication() {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        this.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String text = request.getParameter("result");
        String room = request.getParameter("room");
        if (text != null && text.length() > 0 && room != null && !room.equals("")) {
            CommunicationSocket.sendMessageByOut(room, text);
        }

    }
}
