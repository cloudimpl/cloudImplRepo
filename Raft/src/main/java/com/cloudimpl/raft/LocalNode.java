package com.cloudimpl.raft;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Nuwan on 12/2/2015.
 */
public class LocalNode implements IRaftNode {

    String hostName;
    int port;
    public LocalNode(String hostName,int port)
    {
        this.hostName = hostName;
        this.port = port;
    }
    public String getHostName() throws UnknownHostException {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public long getTerm() {
        return 0;
    }

    public void send(Object msg) throws Exception {

    }
}
