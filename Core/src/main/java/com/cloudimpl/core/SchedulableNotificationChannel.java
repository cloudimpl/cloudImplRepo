/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nuwan
 * @param <E>
 */
public class SchedulableNotificationChannel<E> implements NotificationChannel<E>,Runnable{
    Queue<E> queue;
    PartitionedExecutorService execService;
    AtomicBoolean lock;
    AtomicInteger count;
    NotificationListener<E> listener;
    AtomicBoolean scheduled;
    int partID;
    public SchedulableNotificationChannel(AtomicBoolean lock,PartitionedExecutorService execService,NotificationListener<E> listener,int partID)
    {
        this.partID = partID;
        this.execService = execService;
        this.lock = lock;
        queue = new ConcurrentLinkedQueue<>();
        this.listener = listener;
        count = new AtomicInteger(0);
        scheduled = new AtomicBoolean(false);
    }
    @Override
    public void notify(E notify) throws Exception {
        count.incrementAndGet();
        queue.add(notify);
        if(scheduled.compareAndSet(false, true))
        {
            execService.submit(0, this, null);
        }
    }

    @Override
    public void run() {
        boolean allow = lock.compareAndSet(false, true);
        if(allow)
        {
            E entry = queue.poll();
            while(entry != null)
            {
                count.decrementAndGet();
                try {
                    listener.onNotify(entry);
                    entry = queue.poll();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    listener.onClose(this);
                    break;
                }
            }
            lock.compareAndSet(true, false);
        }
        if(scheduled.compareAndSet(true, false))
        {
            if(count.get() != 0)
            {
                scheduled.compareAndSet(false, true);
                execService.submit(partID, this, null);
            }
        }
    }
    
}
