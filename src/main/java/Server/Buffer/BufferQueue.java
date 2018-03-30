package Server.Buffer;

import org.apache.log4j.Logger;
import java.awt.image.BufferedImage;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 缓存队列
 *
 * Created by hck on 2018/3/23.
 */
public class BufferQueue {
    private static final int MAX_SIZE = 4;
     static Queue<BufferedImage> queue = new ArrayBlockingQueue<BufferedImage>(MAX_SIZE);
    static Logger logger = Logger.getLogger(BufferQueue.class.getName());

    /**
     * 向缓存队列中添加图片
     * @param image
     */
    public static void add(BufferedImage image){
            if(queue.size() < MAX_SIZE){        //  如果队列没有满
                queue.offer(image);                   //  添加
            }else{
                Object obj = queue.poll();            //  删除队列头
                obj = null;
                queue.offer(image);                   //  添加
            }
    }

    /**
     * 获取队列头的图片
     * @return
     */
    public static BufferedImage peek(){
        return queue.peek();
    }

    public static void release(){
        queue.clear();
    }
}