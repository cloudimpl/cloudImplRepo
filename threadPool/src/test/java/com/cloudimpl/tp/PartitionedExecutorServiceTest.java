package com.cloudimpl.tp;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.currentThread;
import static org.junit.Assert.*;

/**
 * Created by Nuwan on 11/29/2015.
 */
public class PartitionedExecutorServiceTest {

    @org.junit.Test
    public void testSubmit() throws Exception {
        int partitions = 10;
        PartitionedExecutorService pool = new PartitionedExecutorService(10,partitions);
        long input = 0;
        final AtomicLong process = new AtomicLong();
        long start = System.currentTimeMillis();
        Set<Long> set = new HashSet<>();
        long vol = 0;
        int part = 0;
        while(true)
        {
            input++;

            if(part < partitions - 1)
            {
                part++;
            }
            else
                part = 0;
            pool.submit(part,new Runnable() {
                @Override
                public void run() {
                    //   System.out.println("thread running : " + currentThread().getId());
                    set.add(currentThread().getId());
                    process.incrementAndGet();
//                    try {
//                        sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }, null);
            //   sleep(1);
            long end = System.currentTimeMillis();
            if(end - start >= 1000) {
                long proc = process.get();
                System.out.println("input " + input + " process " + proc + " dis " + set.size() + " lag " + (input - proc));
                set.clear();
                start = end;
                long st = System.currentTimeMillis();
                while(input != process.get())
                {

                }
                input = 0;
                process.set(0);
                long e = System.currentTimeMillis();
                System.out.println("sync wait "+(e - st) );
                start = e;
                if(vol >= 1000000000)
                    break;
            }
            vol++;

        }

        System.out.println("completed ");

    }
}