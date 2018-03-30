package Client;

import Client.Buffer.ClientBufferQueue;
import Client.Frame.ClientFrame;

import java.io.IOException;

/**
 * Created by hck on 2018/3/28.
 */
public class ClientBoot {
    public static void main(String[] args) throws IOException {
        new Thread(new ClientBufferQueue()).start();
        new Thread(new ClientFrame()).start();
    }
}
