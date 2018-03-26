package test;

import Server.Frame;
import Server.Scanner;

import java.io.IOException;

/**
 * Created by hck on 2018/3/23.
 */
public class TestFrame {
    public static void main(String[] args) throws IOException, InterruptedException {
        new Thread(Scanner.getInstance()).start();

        Frame.init();
//
//        Thread.sleep(10000);
//        Scanner.stopCatchScreen();
    }
}
