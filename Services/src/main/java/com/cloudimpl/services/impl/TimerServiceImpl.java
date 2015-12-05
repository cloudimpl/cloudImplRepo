package com.cloudimpl.services.impl;

import com.cloudimpl.core.NotificationChannel;
import com.cloudimpl.services.Timer;
import com.cloudimpl.services.TimerService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nuwan on 12/5/2015.
 */
public class TimerServiceImpl implements TimerService {
    private ScheduledExecutorService service;

    public TimerServiceImpl(int maxPool)
    {
        service = Executors.newScheduledThreadPool(maxPool);
    }

    @Override
    public Timer createTimer(long val, TimeUnit unit, boolean repeatable, NotificationChannel channel) {
        TimerImpl timer = new TimerImpl(channel);
        ScheduledFuture<?> future;
        if(repeatable)
            future = service.scheduleAtFixedRate(timer, val, val, unit);
        else
            future = service.schedule(timer,val,unit);

        timer.setHnd(future);
        return timer;
    }
}
