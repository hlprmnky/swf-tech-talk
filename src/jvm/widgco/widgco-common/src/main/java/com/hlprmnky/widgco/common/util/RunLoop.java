package com.hlprmnky.widgco.common.util;

import org.slf4j.Logger;

public abstract class RunLoop implements Runnable {

    public abstract void doRunLoop();
    private Logger log;
    private long sleepInterval = 100L;
    private boolean done = false;

    public RunLoop(Logger log, long sleepInterval) {
        this.log = log;
        this.sleepInterval = sleepInterval;
    }

    public void run() {
        log.debug("Starting {}", this.getClass().getCanonicalName());
        while (!done) {
            if(Thread.currentThread().isInterrupted()) {
                log.info("Thread has been interrupted, flagging for shutdown");
                done = true;
            }
            doRunLoop();
            try {
                log.info("Sleeping for {} millis", sleepInterval);
                Thread.sleep(sleepInterval);
            } catch (InterruptedException consumed) {
                log.info("Caught InterruptedException {}, flagging for shutdown", consumed);
                done = true;
                Thread.currentThread().interrupt();
            }
        }
    }
}
