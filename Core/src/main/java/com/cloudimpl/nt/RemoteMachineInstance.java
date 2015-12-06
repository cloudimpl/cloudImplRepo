/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.nt;

import com.cloudimpl.core.LoggerService;
import com.cloudimpl.core.NotificationChannel;
import com.cloudimpl.core.NotificationChannelManager;
import com.cloudimpl.core.NotificationListener;
import com.cloudimpl.core.PartitionedExecutorService;
import com.cloudimpl.core.Timer;
import com.cloudimpl.core.TimerService;
import com.cloudimpl.core.msgs.TransportMsg;
import java.util.concurrent.TimeUnit;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;

/**
 *
 * @author Nuwan
 */
public class RemoteMachineInstance implements IMachineInstance,NotificationListener<Object>{

    TCPTransportEngine engine;
    String hostName;
    String ipAddr;
    int port;
    TimerService service;
    Timer timer;
    IoSession session;
    NotificationChannelManager channelMan;
    NotificationChannel<TransportMsg> senderMsgChannel;
    NotificationChannel<TransportMsg> receiverMsgChannel;
    NotificationChannel<Object> ctrlChannel;
    SenderMsgHandler senderHnd;
    ReceiverMsgHandler receiverHnd;
    Logger logger;
    public RemoteMachineInstance(PartitionedExecutorService service,LoggerService loggerService,TCPTransportEngine engine,TimerService timerService,String ipAddr,int port)
    {
        logger = loggerService.getLogger("mi_"+ipAddr+"_"+port);
        channelMan = new NotificationChannelManager(service, loggerService);
        this.engine = engine;
        senderHnd = new SenderMsgHandler(this);
        receiverHnd = new ReceiverMsgHandler(this);
        ctrlChannel = channelMan.createSchedChannel(this,0);
        timer = timerService.createTimer(5, TimeUnit.SECONDS, true,ctrlChannel);
        senderMsgChannel = channelMan.createSchedChannel(senderHnd,0);
        receiverMsgChannel = channelMan.createSchedChannel(receiverHnd,0);
        this.ipAddr = ipAddr;
        this.port = port;
    }
    
    public NotificationChannel<TransportMsg> getSenderMsgChannel()
    {
        return senderMsgChannel;
    }
    
    public NotificationChannel<TransportMsg> getReceiverMsgChannel()
    {
        return receiverMsgChannel;
    }
    
    public NotificationChannel<Object> getCtrlChannel()
    {
        return ctrlChannel;
    }
    
    @Override
    public void send(TransportMsg msg) throws Exception {
        getSenderMsgChannel().notify(msg);
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onNotify(Object msg) throws Exception {
         if((msg instanceof  Timer) && (this.timer == timer))
        {
            if(session == null)
                session = engine.connnect(ipAddr, port);
            
            if(session != null)
            {
                session.setAttribute("mi", this);
                logger.info("remote machine {} connected"+session.getRemoteAddress());
            }
        }
         else if(msg instanceof IoSession)
         {
             if(session == msg)
             {
                 session = null;
             }
         }
    }

    @Override
    public void onClose(NotificationChannel<Object> channel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void onTransportReceive(TransportMsg msg) {
        System.out.println("msg received "+msg);
        
    }

    private void onTransportSend(TransportMsg msg) {
        System.out.println("sent msg : "+msg);
        if(session != null)
            session.write(msg);
    }
    
    public static final class ReceiverMsgHandler implements NotificationListener<TransportMsg>
    {
        RemoteMachineInstance mi;
        public ReceiverMsgHandler(RemoteMachineInstance mi)
        {
            this.mi = mi;
        }
        @Override
        public void onNotify(TransportMsg msg) throws Exception {
            mi.onTransportReceive(msg);
        }

        @Override
        public void onClose(NotificationChannel<TransportMsg> channel) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    public static final class SenderMsgHandler implements NotificationListener<TransportMsg>
    {
        RemoteMachineInstance mi;
        public SenderMsgHandler(RemoteMachineInstance mi)
        {
            this.mi = mi;
        }
        @Override
        public void onNotify(TransportMsg msg) throws Exception {
            mi.onTransportSend(msg);
        }

        @Override
        public void onClose(NotificationChannel<TransportMsg> channel) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}
