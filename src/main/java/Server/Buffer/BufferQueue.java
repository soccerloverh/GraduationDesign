package Server.Buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 缓存队列
 * <p>
 * Created by hck on 2018/3/23.
 */
public class BufferQueue {
    private static final int MAX_SIZE = 16;
    static Queue<byte[]> queue = new LinkedList<byte[]>();
    static ByteArrayOutputStream out = new ByteArrayOutputStream(307200);   // 300KB
    static Logger logger = Logger.getLogger(BufferQueue.class.getName());

    /**
     * 向缓存队列中添加图片
     *
     * @param image
     */
    public static void add(BufferedImage image) {
        try {
            ImageIO.write(image,"png",out);
        } catch (Exception e) {
            out.reset();
            return;
        }
        if (queue.size() < MAX_SIZE) {                                     //  如果队列没有满
            queue.offer(wrapPackage(out.toByteArray()));                   //  添加
        } else {
            Object obj = queue.poll();                                     //  删除队列头
            obj = null;
            queue.offer(wrapPackage(out.toByteArray()));                   //  添加
        }
    }

    private static byte[] wrapPackage(byte[] res){
        byte[] bytes = new byte[res.length+2];
        System.arraycopy(res,0,bytes,0,res.length);
        bytes[bytes.length-1] = -128;
        bytes[bytes.length-2] = -128;
        out.reset();
        return bytes;
    }

    /**
     * 获取队列头的图片
     *
     * @return
     */
    public static byte[] peek() {
        return queue.peek();
    }

    public static boolean isEmpty(){
        return queue.isEmpty();
    }

    public static void release() {
        queue.clear();
        System.gc();
    }
}
