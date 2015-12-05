package com.cloudimpl.services;

import com.cloudimpl.core.NotificationChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nuwan on 12/5/2015.
 */
public interface TimerService {

    Timer createTimer(long val,TimeUnit unit,boolean repeatable,NotificationChannel channel);
}
