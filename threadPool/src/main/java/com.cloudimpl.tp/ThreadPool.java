package com.cloudimpl.tp;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Nuwan on 11/29/2015.
 */
public class ThreadPool extends Thread{

    PartitionPool[] arrPool;
    public ThreadPool(int max,int part)
    {
        arrPool = new PartitionPool[part];
        int i = 0;
        while(i < arrPool.length)
        {
            arrPool[i] = new PartitionPool(max);
            i++;
        }

        start();
    }

    public ThreadPool(int max)
    {
        this(max,1);
    }

    public void submit(int part,Runnable runnable,Object userdata)
    {
        arrPool[part].submit(runnable, userdata);
    }

    public void submit(Runnable runnable,Object userdata)
    {
        arrPool[0].submit(runnable,userdata);
    }
    @Override
    public void run()
    {
        int wait = 0;
        while(true)
        {
            for(PartitionPool pool : arrPool)
            {
                if(pool.getPendingJobCount() != 0)
                {
                    pool.setWait(pool.getWait() + 1);
                    if(pool.getWait() > 1000)
                    {
                        if(pool.isIdleQueueEmpty() && pool.getAlloc() < pool.getMax())
                        {
                            pool.createThread();
                        }
                        else
                        {
                            pool.wakeupIdle();
                        }
                        pool.setWait(0);
                    }
                }
            }

        }
    }

    public static final class PartitionPool
    {
        private int alloc = 0;
        private final int max;
        private Queue<Tuple<Runnable,Object>> queue;
        private AtomicInteger counter = new AtomicInteger(0);
        private Queue<ThreadEx> idles = new ConcurrentLinkedQueue<>();
        private int wait = 0;
        public PartitionPool(int max)
        {
            queue = new ConcurrentLinkedQueue<>();
            this.max = max;

        }

        public int getWait()
        {
            return wait;
        }

        public int setWait(int count)
        {
            wait = count;
            return wait;
        }
        public int getPendingJobCount()
        {
            return counter.get();
        }

        public int getAlloc()
        {
            return alloc;
        }

        public int getMax()
        {
            return max;
        }

        public boolean isIdleQueueEmpty()
        {
            return idles.isEmpty();
        }

        public void putIdle(ThreadEx t)
        {
            idles.add(t);
        }
        public void submit(Runnable runnable,Object userdata)
        {
            counter.incrementAndGet();
            queue.add(new Tuple<>(runnable, userdata));
        }

        public Tuple<Runnable,Object> get()
        {
            return queue.poll();
        }

        public ThreadEx createThread()
        {
            ThreadEx t = new ThreadEx(this);
            t.start();
            alloc++;
            return t;
        }

        public void wakeupIdle()
        {
            ThreadEx t   = idles.poll();
            if(t != null)
                t.wakeup();
        }
    }
}
