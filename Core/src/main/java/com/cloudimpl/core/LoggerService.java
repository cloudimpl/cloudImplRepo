/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.core;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;

/**
 *
 * @author Nuwan
 */
public interface LoggerService {
   Logger getLogger(String logger);
   void setLevel(String logger,Level level);
   static Logger getLogger(LoggerService service,String loggerName)
   {
       return service.getLogger(loggerName);
   }
}
