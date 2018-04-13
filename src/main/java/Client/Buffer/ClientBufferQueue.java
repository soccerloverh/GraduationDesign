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
    private static ByteArrayOutputStream out = new ByteArrayOutputStream(307200);
    /**
     * 图片byte缓存
     */
    public static ArrayBlockingQueue<byte[]> imageByte_queue = new ArrayBlockingQueue(128);
    /**
     * 缓冲组包好的Image
     */
    public static ArrayBlockingQueue<BufferedImage> img_queue = new ArrayBlockingQueue(128);

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
                if (img_queue.remainingCapacity() > 0) {
//                    imageByte_queue.offer(out.toByteArray());
                    img_queue.offer(ImageIO.read(new ByteArrayInputStream(out.toByteArray())));
                } else {
                    img_queue.poll();
//                    imageByte_queue.offer(out.toByteArray());
                    img_queue.offer(ImageIO.read(new ByteArrayInputStream(out.toByteArray())));
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
    public void run() {    //   不停尝试从图片字节数组缓冲中获取图片 并生成BufferedImage 放入图片队列
        for (; ; ) {
            try {
                if (imageByte_queue.isEmpty()) {
                    Thread.sleep(200);
                } else {
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageByte_queue.poll()));    //  从byte 缓冲区获取图片
                    img_queue.offer(image);                                                             //  写入图片缓冲
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                continue;
            }
        }

    }
}
