/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.nt;

import com.cloudimpl.core.LoggerService;
import com.cloudimpl.core.TransportEngine;
import com.cloudimpl.core.msgs.TransportMsg;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;

/**
 *
 * @author Nuwan
 */
public class TCPTransportEngine extends IoHandlerAdapter implements TransportEngine{

    NioSocketConnector connector;
    Logger logger;
    MachineManager machineManager;
    public TCPTransportEngine(LoggerService logService,MachineManager machineManager) {
        this.machineManager = machineManager;
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(4000);
        connector.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        logger = LoggerService.getLogger(logService, "rmi");
        connector.getFilterChain().addLast("logger", new LoggingFilter("rmi"));
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);
        connector.setHandler(this);
    }
    
    public IoSession connnect(String ipAddr,int port) throws InterruptedException
    {
        ConnectFuture future = connector.connect(new InetSocketAddress(ipAddr, port));
        future.await(4, TimeUnit.SECONDS);
        return future.getSession();   
    }

    @Override
    public void send(TransportMsg transportMsg) throws Exception {
        IMachineInstance mi = machineManager.getMachine(transportMsg.getReceiver());
        if(mi != null)
        {
            mi.send(transportMsg);
        }
    }
    
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        // Empty handler
        
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        RemoteMachineInstance mi = (RemoteMachineInstance) session.getAttribute("mi");
        if(mi != null)
        {
            mi.getCtrlChannel().notify(session);
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // Empty handler
        RemoteMachineInstance mi = (RemoteMachineInstance) session.getAttribute("mi");
        if(mi == null)
        {
            logger.warn("session {} remove due to not remote machine instance attached to it.",session.getRemoteAddress());
            session.close(true);
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        session.close(true);
        RemoteMachineInstance mi = (RemoteMachineInstance) session.getAttribute("mi");
        if(mi != null)
        {
            mi.getCtrlChannel().notify(session);
        }
    }   

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
         RemoteMachineInstance mi = (RemoteMachineInstance) session.getAttribute("mi");
         if(message instanceof TransportMsg)
         {
            if(mi != null)
            {
                mi.getReceiverMsgChannel().notify((TransportMsg) message);
            }
         }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // Empty handler
    }
}
