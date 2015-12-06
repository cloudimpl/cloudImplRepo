/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

import com.cloudimpl.core.msgs.TransportMsg;
import com.google.common.util.concurrent.RateLimiter;

/**
 *
 * @author Nuwan
 */
public class TransportChannel {
    public enum PROTO {TCP,HTTP}
    String sender;
    String target;
    int rate;
    RateLimiter rateLimiter;
    TransportEngine engine;
    public TransportChannel(TransportEngine engine,String senderName,String destination,int rate)
    {
        this.engine = engine;
        this.target = destination;
        this.sender = senderName;
        this.rate = rate;
        if(rate <= 0)
            rateLimiter = RateLimiter.create(Integer.MAX_VALUE);
        else
            rateLimiter = RateLimiter.create(rate);
    }
    
    
    public void send(Object  msg) throws Exception
    {
        rateLimiter.acquire();
        engine.send(new TransportMsg(sender, target,msg));
    }
}
