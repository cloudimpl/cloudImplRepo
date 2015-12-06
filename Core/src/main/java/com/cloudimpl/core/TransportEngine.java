/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

import com.cloudimpl.core.msgs.TransportMsg;

/**
 *
 * @author Nuwan
 */
public interface TransportEngine {
    void send(TransportMsg transportMsg)throws Exception;
}
