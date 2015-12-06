/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.nt;

import com.cloudimpl.core.LoggerService;
import com.cloudimpl.core.PartitionedExecutorService;
import com.cloudimpl.core.TimerService;
import com.cloudimpl.core.msgs.TransportMsg;
import com.cloudimpl.services.impl.LogBackLoggerService;
import com.cloudimpl.services.impl.TimerServiceImpl;
import java.io.IOException;

/**
 *
 * @author Nuwan
 */
public class App {
    public static void main(String[] args) throws IOException, Exception {
        LoggerService loggerService = new LogBackLoggerService(false, "E:/test.log");
        TimerService timerService = new TimerServiceImpl(10);
        PartitionedExecutorService service = new PartitionedExecutorService(loggerService.getLogger("teset"), 5, 2);
        LocalMachineInstance mi = new LocalMachineInstance(service, loggerService, 5000, "localhost", "127.0.0.1");
        TCPTransportEngine engine = new TCPTransportEngine(loggerService, null);
        RemoteMachineInstance mi2 = new RemoteMachineInstance(service, loggerService, engine, timerService,"127.0.0.1",5000);
        Thread.sleep(3000);
        int i = 0;
        while(true)
        {
            mi2.send(new TransportMsg("wooo","tooo","nuwan"+i));
            i++;
            Thread.sleep(1000);
        }
    }
}
