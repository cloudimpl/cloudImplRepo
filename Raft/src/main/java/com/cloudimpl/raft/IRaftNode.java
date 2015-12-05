package com.cloudimpl.raft;

import java.net.UnknownHostException;

/**
 * Created by Nuwan on 12/2/2015.
 */
public interface IRaftNode {
    String getHostName() throws UnknownHostException;
    int getPort();
    long getTerm();
    void send(Object msg) throws Exception;
}
