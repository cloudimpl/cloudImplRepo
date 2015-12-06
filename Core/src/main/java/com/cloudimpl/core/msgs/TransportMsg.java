/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core.msgs;

import java.io.Serializable;

/**
 *
 * @author Nuwan
 */
public class TransportMsg implements Serializable{
    private final String sender;
    private final String receiver;
    private final Object msg;

    public TransportMsg(String sender, String receiver, Object msg) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
    }

    public Object getMsg() {
        return msg;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "TransportMsg{" + "sender=" + sender + ", receiver=" + receiver + ", msg=" + msg + '}';
    }
    
}
    
