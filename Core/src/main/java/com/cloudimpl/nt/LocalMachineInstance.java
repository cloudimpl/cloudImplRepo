/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.nt;

import com.cloudimpl.core.LoggerService;
import com.cloudimpl.core.NotificationChannelManager;
import com.cloudimpl.core.PartitionedExecutorService;
import com.cloudimpl.core.msgs.TransportMsg;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author Nuwan
 */
public class LocalMachineInstance extends IoHandlerAdapter implements IMachineInstance{
    
    int port;
    String host;
    String ip;
    IoAcceptor acceptor;
    LoggerService logService;
    NotificationChannelManager channelMan;
    public LocalMachineInstance(PartitionedExecutorService service,LoggerService logService,int port, String host, String ip) throws IOException {
        this.port = port;
        this.host = host;
        this.ip = ip;
        channelMan = new NotificationChannelManager(service, logService);
        acceptor = new NioSocketAcceptor();
        LoggerService.getLogger(logService, "mi_"+host+"_"+port);
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter("mi_"+host+"_"+port) );
        acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        acceptor.setHandler(this);
        acceptor.bind( new InetSocketAddress(port) );
    }
    
    
    @Override
    public void send(TransportMsg msg) {

    }
    
    
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        // Empty handler
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // Empty handler
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // Empty handler
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        session.close(true);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        // Empty handler
        System.out.println("received "+message);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // Empty handler
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
