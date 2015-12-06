/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Nuwan
 */
public class NotificationChannelManager {
    PartitionedExecutorService service;
    LoggerService loggerService;
    AtomicBoolean lock;
    public NotificationChannelManager(PartitionedExecutorService service,LoggerService loggerService)
    {
        this.service = service;
        this.loggerService = loggerService;
        lock = new AtomicBoolean(false);
    }
    
    
    public <E> NotificationChannel<E> createSchedChannel(NotificationListener<E> listener,int partID)
    {
        return new SchedulableNotificationChannel<>(lock,service,listener,partID);
    }
}
