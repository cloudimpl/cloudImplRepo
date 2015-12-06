/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.nt;

import com.cloudimpl.core.msgs.TransportMsg;

/**
 *
 * @author Nuwan
 */
public interface IMachineInstance {
    void send(TransportMsg msg)throws Exception;
    void close();
}
