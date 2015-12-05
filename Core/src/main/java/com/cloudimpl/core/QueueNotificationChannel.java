package com.cloudimpl.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Nuwan on 12/5/2015.
 */
public class QueueNotificationChannel<T> implements NotificationChannel<T> {
    Queue<T> queue = new ConcurrentLinkedQueue<>();
    @Override
    public void notify(T notify) throws Exception {
            queue.add(notify);
    }
}
