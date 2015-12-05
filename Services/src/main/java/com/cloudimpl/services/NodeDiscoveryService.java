package com.cloudimpl.services;

import com.cloudimpl.core.NotificationChannel;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Nuwan on 12/2/2015.
 */
public abstract class NodeDiscoveryService implements Runnable{

    Thread t = null;
    NotificationChannel notificationChannel;
    public NodeDiscoveryService(NotificationChannel channel)
    {
        this.notificationChannel = channel;
    }

    public NotificationChannel getNotifyChannel()
    {
        return this.notificationChannel;
    }

    public abstract void init()throws Exception;

    public abstract void run();

    public abstract void close();

    public abstract  NodeInfoPublisher getPublisher() throws SocketException, UnknownHostException;

    public final void start()throws Exception
    {
        if(t != null)
            throw new Exception("machine discovery service already started");
        init();
        t = new Thread(this);
        t.start();
    }

    public void shutdown()
    {
        if(t == null)
            return;
        close();

    }

    public static interface  NodeInfoPublisher
    {
        void send(String host,int port) throws Exception;
        void close();
    }
}
