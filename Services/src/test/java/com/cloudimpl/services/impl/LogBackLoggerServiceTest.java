/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudimpl.services.impl;

import ch.qos.logback.classic.Level;
import com.cloudimpl.core.LoggerService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;

/**
 *
 * @author Nuwan
 */
public class LogBackLoggerServiceTest {
    
    public LogBackLoggerServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getLogger method, of class LogBackLoggerService.
     */
    @Test
    public void testGetLogger() {
        LoggerService service = new LogBackLoggerService(false,"E:/test.log","%date|%level|[%thread]|%logger{10}|[%file:%line]|%msg%n");
        Logger logger = service.getLogger("match-aapl");
        logger.debug("this is debug");
     //   service.setLevel("match-aapl", Level.INFO);
        logger.debug("this is second {} debug","nuwan");
    }
    
}
