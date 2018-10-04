package entity;

public class Answer {
    String num;
    String result;

    public Answer(String num, String result) {
        this.num = num;
        this.result = result;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "num=" + num +
                ", result='" + result + '\'' +
                '}';
    }
}
