/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

/**
 *
 * @author Nuwan
 */
public class TransportChannelBuilder {
    TransportChannel.PROTO proto;
    String sender;
    String target;
    int rate;
    int max;
    public TransportChannel create(TransportChannel.PROTO proto,String sender,String target,int rate)
    {
        return new TransportChannel(null,sender, target, rate);
    }
}
