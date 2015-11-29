package com.cloudimpl.tp;


/**
 * Created by Nuwan on 11/29/2015.
 */
public class Worker extends Thread {


    PartitionedExecutorService.PartitionPool pool;
    boolean needWakeup = false;
    public Worker(PartitionedExecutorService.PartitionPool pool)
    {
        this.pool = pool;

    }

    public void wakeup()
    {
        synchronized (this)
        {
            if(needWakeup)
                notify();
        }
    }

    @Override
    public void run()
    {
        int wait = 0;
        while(true)
        {

            Tuple<Runnable,Object> tuple = null;
            try {

                tuple = pool.get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if(tuple != null) {
                tuple.getK().run();
            }
            else {
                wait++;
                if(wait >= 1000)
                {
                    synchronized (this) {
                        try {
                            needWakeup = true;
                            pool.putIdle(this);
                            wait();
                            needWakeup = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    wait = 0;
                }
            }
//            else
//                try {
//                    synchronized (this) {
//                        wait();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            //   pool.addToPool(this);
        }

    }

}
