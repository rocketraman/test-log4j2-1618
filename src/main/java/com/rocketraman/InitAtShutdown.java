package com.rocketraman;

import java.util.logging.Logger;

class InitAtShutdown {
    static {
        System.err.println("InitAtShutdown class init");
    }
    private static final Logger logger = Logger.getLogger(InitAtShutdown.class.getName());
}
