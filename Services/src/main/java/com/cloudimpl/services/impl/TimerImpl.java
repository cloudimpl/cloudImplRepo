package com.cloudimpl.services.impl;

import com.cloudimpl.core.NotificationChannel;
import com.cloudimpl.services.Timer;

import java.util.concurrent.ScheduledFuture;

/**
 * Created by Nuwan on 12/5/2015.
 */
public class TimerImpl implements Timer,Runnable {
    boolean cancel = false;
    ScheduledFuture hnd;
    NotificationChannel channel;
    public TimerImpl(NotificationChannel channel)
    {
        this.channel = channel;
    }
    @Override
    public void stop() {
        cancel = true;
        if(hnd != null) {
            hnd.cancel(true);
            hnd = null;
        }

    }

    @Override
    public void run() {
        if(cancel) {
            if (hnd != null) {
                hnd.cancel(true);
                hnd = null;
            }
        }
        try {
            channel.notify(this);
        } catch (Exception e) {
            stop();
        }
    }

    void setHnd(ScheduledFuture future)
    {
        this.hnd = future;
        if(cancel) {
            hnd.cancel(true);
            hnd = null;
        }
    }
}
