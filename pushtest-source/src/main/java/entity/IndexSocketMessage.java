package entity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IndexSocketMessage {
    DirecEnum direction;
    String room;
    String text;
    String img;
    String imgSerialNum;
    Set<String> allImageState;
    String textNum;
    String imgNum;
    String command;
    Answer answers;

    public IndexSocketMessage() {
    }

    public IndexSocketMessage clear() {
        this.direction = null;
        this.room = null;
        this.text = null;
        this.img = null;
        this.imgSerialNum = null;
        this.allImageState = null;
        this.textNum = null;
        this.imgNum = null;
        this.command = null;
        return this;
    }


    public boolean directionIsNull() {
        return this.direction == null;
    }

    public boolean textIsNull() {
        return this.text == null;
    }

    public boolean imgIsNull() {
        return this.img == null;
    }

    public boolean allImageStateIsNull() {
        return this.allImageState == null;
    }

    public boolean textNumIsNull() {
        return this.textNum == null;
    }

    public boolean imgNumIsNull() {
        return this.imgNum == null;
    }

    public boolean commandIsNull() {
        return this.command == null;
    }

    public boolean imgSerialNumIsNull() {
        return this.imgSerialNum == null;
    }

    public boolean roomIsNull() {
        return this.room == null && this.room.matches("^[0-9a-zA-Z]+$");
    }

    public boolean answersIsNull() {
        if (this.answers == null) return true;
        if (this.answers.num == null || this.answers.num.equals("") || this.answers.getResult() == null || this.answers.getResult().equals(""))
            return true;
        return false;
    }

    public DirecEnum getDirection() {
        return this.direction;
    }

    public IndexSocketMessage setDirection(DirecEnum direction) {
        this.direction = direction;
        return this;
    }

    public String getRoom() {
        return this.room;
    }

    public IndexSocketMessage setRoom(String room) {
        this.room = room;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public IndexSocketMessage setText(String text) {
        this.text = text;
        return this;
    }

    public String getImg() {
        return this.img;
    }

    public IndexSocketMessage setImg(String img) {
        this.img = img;
        return this;
    }

    public String getImgSerialNum() {
        return this.imgSerialNum;
    }

    public IndexSocketMessage setImgSerialNum(String imgSerialNum) {
        this.imgSerialNum = imgSerialNum;
        return this;
    }

    public Set<String> getAllImageState() {
        return this.allImageState;
    }

    public IndexSocketMessage setAllImageState(Set<String> allImageState) {
        this.allImageState = allImageState;
        return this;
    }

    public String getTextNum() {
        return this.textNum;
    }

    public IndexSocketMessage setTextNum(String textNum) {
        this.textNum = textNum;
        return this;
    }

    public String getImgNum() {
        return this.imgNum;
    }

    public IndexSocketMessage setImgNum(String imgNum) {
        this.imgNum = imgNum;
        return this;
    }

    public String getCommand() {
        return this.command;
    }

    public IndexSocketMessage setCommand(String command) {
        this.command = command;
        return this;
    }

    public Answer getAnswers() {
        return answers;
    }

    public IndexSocketMessage setAnswers(Answer answers) {
        this.answers = answers;
        return this;
    }


    public String toString() {
        return "IndexSocketMessage{direction=" + this.direction + ", room='" + this.room + '\'' + ", text='" + this.text + '\'' + ", img='" + this.img + '\'' + ", imgSerialNum='" + this.imgSerialNum + '\'' + ", allImageState=" + this.allImageState + ", textNum='" + this.textNum + '\'' + ", imgNum='" + this.imgNum + '\'' + ", command='" + this.command + '\'' + '}';
    }


}
