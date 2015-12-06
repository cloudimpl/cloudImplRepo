/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

import com.cloudimpl.services.impl.LogBackLoggerService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Nuwan
 */
public class SchedulableNotificationChannelTest {
    
    public SchedulableNotificationChannelTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of notify method, of class SchedulableNotificationChannel.
     */
    @Test
    public void testNotify() throws Exception {
       LoggerService loggerService = new LogBackLoggerService(false, "E:/test.log", "%date|%level|[%thread]|%logger{10}|[%file:%line]|%msg%n");
       PartitionedExecutorService service = new PartitionedExecutorService(loggerService.getLogger("test"), 10,2);
       
        AtomicInteger counter = new AtomicInteger(0);
       
        AtomicBoolean lock = new AtomicBoolean(false);
        AtomicBoolean lock2 = new AtomicBoolean(false);
        ListenerImpl impl = new ListenerImpl(counter);
       NotificationChannel<String> channel = new SchedulableNotificationChannel<>(lock,service,impl,0);
       NotificationChannel<String> channel2 = new SchedulableNotificationChannel<>(lock2,service,impl,1);
       int i = 0;
       long start = System.currentTimeMillis();
       int b = 0;
       while(true)
       {
           channel.notify(new String("abc"));
           channel2.notify("test");
           i+=2;
           b++;
           long end = System.currentTimeMillis();
           if(end - start >= 1000)
           {
               int j = counter.get();
               System.out.println("in "+i+" proc "+j+" lag "+(i - j));
               start = System.currentTimeMillis();
               while(i != counter.get())
               {
                   
               }
               end = System.currentTimeMillis();
               System.out.println("complete "+(end -start)+" k "+impl.k);
            //   assertEquals(i, impl.k);
               start = end;
               i = 0;
               impl.k = 0;
               counter.set(0);
               //if(b >= 100000000)
                 //  break;
           }
       }
    }

  
}
