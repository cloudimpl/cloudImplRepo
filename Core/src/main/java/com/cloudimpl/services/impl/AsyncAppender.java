/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.services.impl;

import ch.qos.logback.core.FileAppender;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Nuwan
 */
public class AsyncAppender<E> extends FileAppender<E> {

    BlockingQueue<E> logs = new LinkedBlockingQueue<>();
    ExecutorService service = Executors.newFixedThreadPool(1);

    public AsyncAppender()
    {
        service.execute(new LogWriter(this));
    }
    
    @Override
    protected void writeOut(E event) throws IOException {
        logs.add(event);
    }
    
    void superWrite(E e)throws IOException
    {
        super.writeOut(e);
    }
    
    public static final class LogWriter<E> implements Runnable
    {
        AsyncAppender<E> appender;
        public LogWriter(AsyncAppender appender)
        {
            this.appender = appender;
        }
        
        @Override
        public void run() {
            while(true)
            {
                try {
                    E e = appender.logs.take();
                    appender.superWrite(e);
                } catch (Exception ex) {
                    
                } 
            }
        }
        
    }
}
