package com.rocketraman;

import org.apache.logging.log4j.LogManager;

public class Log4j1618 {
    private final Object lock = new Object();
    private boolean terminated = false;

    private void registerHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("** Doing shutdown");
                shutdown();
                System.err.println("** Shutdown done");
            }
        });
    }

    private void awaitTermination() throws InterruptedException {
        while(!terminated) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    private void shutdown() {
        synchronized (lock) {
            terminated = true;
            lock.notifyAll();
        }
        // give log4j a chance to shutdown, simulate a little more work here
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            /*ignore*/
        }
        new InitAtShutdown();
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

        org.apache.logging.log4j.Logger logger = LogManager.getLogger();
        logger.info("Log4j logger initialized and working.");
        java.util.logging.Logger.getLogger("main").info("JUL logging via log4j initialized and working.");

        Log4j1618 l = new Log4j1618();
        l.registerHook();

        logger.info("Hook registered, now Ctrl-C the JVM.");
        l.awaitTermination();
    }
}
