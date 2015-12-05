package com.cloudimpl.raft;

/**
 * Created by Nuwan on 12/2/2015.
 */
public class RaftServer {
    private int port;
    private String hostName;

    public RaftServer(String hostName,int port)
    {
        this.hostName = hostName;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostName;
    }
}
