package test;

import Server.Scanner.Scanner;

/**
 * Created by hck on 2018/3/23.
 */
public class TestScanner {
    public static void main(String[] args) throws InterruptedException {
        new Thread(Scanner.getInstance()).start();
        Scanner.startCatchScreen();
        Thread.sleep(20000);
        Scanner.stopCatchScreen();
        System.exit(0);
    }
}
