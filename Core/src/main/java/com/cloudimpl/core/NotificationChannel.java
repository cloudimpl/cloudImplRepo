package com.cloudimpl.core;

/**
 * Created by Nuwan on 12/5/2015.
 */
public interface NotificationChannel<T> {
      void notify(T notify) throws Exception;
}
