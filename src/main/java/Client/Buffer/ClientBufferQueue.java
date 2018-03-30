package Client.Buffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by hck on 2018/3/28.
 */
public class ClientBufferQueue implements Runnable {


    /**
     * 缓冲被传输层协议分割的数据包
     */
    private static ByteArrayOutputStream out = new ByteArrayOutputStream();
    /**
     * 图片byte缓存
     */
    public static ArrayBlockingQueue<byte[]> imageByte_queue = new ArrayBlockingQueue(125);
    /**
     * 缓冲组包好的Image
     */
    public static ArrayBlockingQueue<BufferedImage> img_queue = new ArrayBlockingQueue(32);

    /**
     * 1.0 协议: 规定包尾标识位为两个 -128
     *
     * @param bytes
     */
    public static void add(byte[] bytes) {
        try {
            int len = bytes.length;
            if (bytes[len - 1] == -128 && bytes[len - 2] == -128) {
                out.write(bytes, 0, len - 2);
                if (imageByte_queue.remainingCapacity() > 0) {
                    imageByte_queue.offer(out.toByteArray());
                } else {
                    imageByte_queue.poll();
                    imageByte_queue.offer(out.toByteArray());
                }
                out.reset();
            } else {
                out.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        for (; ; ) {
            try {
                if (imageByte_queue.isEmpty()) {
                    Thread.sleep(200);
                } else {
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageByte_queue.poll()));    //  从byte 缓冲区获取图片
                    img_queue.offer(image);                                                                  //  写入图片缓冲
                    Thread.sleep(20);
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                continue;
            }
        }

    }
}
