package com.cloudimpl.raft;

import com.cloudimpl.core.NotificationChannel;
import com.cloudimpl.core.Tuple;

/**
 * Created by Nuwan on 12/2/2015.
 */
public  abstract class INodeListener implements NotificationChannel<Tuple<String,Integer>> {

    public void notify(Tuple<String,Integer> notify) throws Exception {

        onNodeInfo(notify.getK(),notify.getV());
    }

    public abstract void onNodeInfo(String remoteIp,int remotePort);


}
