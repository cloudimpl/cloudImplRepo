/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Nuwan
 */
public class ServiceRegistry {
    private Map<Integer,Tuple<String,Integer>> map = new ConcurrentHashMap<>();
    
    public void put(Integer serviceID,String ip,int port)
    {
        map.put(serviceID, new Tuple<>(ip,port));
    }
    
    
}
