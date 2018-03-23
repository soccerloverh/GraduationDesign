package Server;

import org.apache.log4j.Logger;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 缓存队列
 *
 * Created by hck on 2018/3/23.
 */
public class BufferQueue {
    private static final int MAX_SIZE = 4;
    private static Queue<BufferedImage> queue = new LinkedList<BufferedImage>();
    static Logger logger = Logger.getLogger(BufferQueue.class.getName());
    static ByteArrayOutputStream out = new ByteArrayOutputStream();

    /**
     * 向缓存队列中添加图片
     * @param image
     */
    public static void add(BufferedImage image){
            if(queue.size() <= MAX_SIZE){        //  如果队列没有满
                queue.offer(image);                   //  添加
                out.reset();                          //  重置流
            }else{
                queue.poll();                         //  删除队列头
                queue.offer(image);                   //  添加
                out.reset();                          //  重置流
            }
    }

    /**
     * 获取队列头的图片
     * @return
     */
    public static BufferedImage peek(){
        return queue.peek();
    }
}
