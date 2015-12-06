/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.services.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import com.cloudimpl.core.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nuwan
 */
public class LogBackLoggerService implements LoggerService {

    FileAppender<ILoggingEvent> fileAppender;
    
    public LogBackLoggerService(boolean async,String loggerPath, String pattern) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern(pattern);
        ple.setContext(lc);
        ple.start();
        if(async)
            fileAppender = new AsyncAppender<>();
        else
            fileAppender = new FileAppender<>();
        fileAppender.setFile(loggerPath);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();
    }

    public LogBackLoggerService(boolean async,String loggerPath)
    {
        this(async,loggerPath,"%date|%level|[%thread]|%logger{10}|[%file:%line]|%msg%n");
    }
    @Override
    public Logger getLogger(String loggerName) {

        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(loggerName);
        logger.addAppender(fileAppender);
        logger.setAdditive(true);
        return logger;
    }

    @Override
    public void setLevel(String loggerName, Level level) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(loggerName);
        logger.setLevel(level);
    }

}
