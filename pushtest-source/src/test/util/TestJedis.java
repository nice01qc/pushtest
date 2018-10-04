package util;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.*;
import java.util.Map;

public class TestJedis {

    @Test
    public void testone() throws IOException {
        Jedis jedis = new Jedis("localhost",6379);

        System.out.println(jedis.keys("*"));

//        System.out.println(jedis.set("nice","23"));
        ScanResult<Map.Entry<String,String>> scanResult = jedis.hscan("hset","1");
        System.out.println(scanResult.getResult());
        System.out.println(ScanParams.SCAN_POINTER_START);
        System.out.println((char)ScanParams.SCAN_POINTER_START_BINARY[0]);

        ByteOutputStream byteBuffer = new ByteOutputStream();
        System.out.println(byteBuffer.size());
        ObjectOutputStream objectOutputStream = new ObjectOutputStream( byteBuffer);
        objectOutputStream.writeObject(new String("1212122"));

        ByteInputStream byteInputStream = new ByteInputStream(byteBuffer.getBytes(),byteBuffer.size());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);

        try {
            System.out.println((String)objectInputStream.readObject());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
