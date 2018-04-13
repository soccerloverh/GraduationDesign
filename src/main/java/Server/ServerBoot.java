package Server;

import Server.Frame.Frame;
import Server.Net.Server;
import Server.Scanner.Scanner;

/**
 * Created by hck on 2018/3/28.
 */
public class ServerBoot {
    public static void main(String[] args) {
        new Thread(Scanner.getInstance()).start();      // 开启采集器
        new Frame();                // 创建界面
        Server.start();
    }
}
