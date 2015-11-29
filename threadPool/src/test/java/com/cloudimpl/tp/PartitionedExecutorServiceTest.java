/*
 * Copyright [2015] [Nuwan Sanjeewa Abeysiriwardana]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
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
        int partitions = 2;
        PartitionedExecutorService pool = new PartitionedExecutorService(2,partitions);
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