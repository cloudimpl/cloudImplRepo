/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Nuwan
 */
public class ListenerImpl implements NotificationListener<String>{
    AtomicInteger counter;
    int k = 0;
    ListenerImpl(AtomicInteger counter)
    {
        this.counter = counter;
    }
    @Override
    public void onNotify(String msg) throws Exception {
        k++;
        counter.incrementAndGet();
    }

    @Override
    public void onClose(NotificationChannel<String> channel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
