package com.cloudimpl.services.impl;

import com.cloudimpl.core.NotificationChannel;
import com.cloudimpl.core.Tuple;
import com.cloudimpl.core.NodeDiscoveryService;

import java.io.IOException;
import java.net.*;
import java.text.MessageFormat;

/**
 * Created by Nuwan on 12/2/2015.
 */
public class MulticastNodeDiscoveryService extends NodeDiscoveryService {
    String multicastIP;
    int port;
    MulticastSocket receiver;
    boolean exit = false;
    public MulticastNodeDiscoveryService(String multicastIP, int port, NotificationChannel listener) {
        super(listener);
        this.multicastIP = multicastIP;
        this.port = port;

    }

    @Override
    public void init() throws Exception {
        receiver = new MulticastSocket(port);
        InetAddress group = InetAddress.getByName(multicastIP);
        receiver.joinGroup(group);
    }

    @Override
    public void run() {
        while(!exit)
        {
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                receiver.receive(packet);
                String received = new String(packet.getData());
                received = received.trim();
                if(!received.isEmpty())
                    onNodeInfo(received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        receiver.close();
    }

    private void onNodeInfo(String nodeInfo) {
        String[] str = nodeInfo.split(":");
        if(str.length != 2)
            return;
        int port = 0;
        try
        {
             port = Integer.valueOf(str[1]);
            getNotifyChannel().notify(new Tuple<String, Integer>(str[0], port));
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
        exit = true;
    }

    @Override
    public NodeInfoPublisher getPublisher() throws SocketException, UnknownHostException {
        return new MulticastPublisher(multicastIP,port);
    }

    public static final class  MulticastPublisher implements NodeInfoPublisher
    {
        DatagramSocket sender;
        String multicastAddr;
        int port;
        InetAddress group;
        public MulticastPublisher(String multicastAddr,int port) throws SocketException, UnknownHostException {
            sender = new DatagramSocket();
            this.multicastAddr = multicastAddr;
            this.port = port;
            group = InetAddress.getByName(multicastAddr);
        }

        @Override
        public void send(String host, int port) throws Exception {
            String msg = MessageFormat.format("{0}:{1}",host, Long.toString(port));
            DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),msg.length(),group,this.port);
            sender.send(msgPacket);
        }

        public void close()
        {
            sender.close();
        }
    }
}
