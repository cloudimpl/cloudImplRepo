/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

/**
 *
 * @author Nuwan
 * @param <E>
 */
public interface NotificationListener<E> {
    
    void onNotify(E msg) throws Exception;
    void onClose(NotificationChannel<E> channel);
}
