package test;

import org.apache.log4j.Logger;

/**
 * Created by hck on 2018/3/23.
 */
public class TestLogger {
    static Logger logger = Logger.getLogger(TestLogger.class);

    public static void main(String[] args) {
        logger.debug("This is debug info");
        logger.info("This is Info info");
        logger.warn("This is warn info");
        logger.error("This is error info");
    }
}
