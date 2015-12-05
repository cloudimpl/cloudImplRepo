package com.cloudimpl.vm;

import io.atomix.AtomixServer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Nuwan on 11/30/2015.
 */
public class App {
    public static void main(String[] args) {
        Address address = new Address("127.0.0.1", 5000);

        List<Address> members = Arrays.asList(

        );

        AtomixServer.Builder builder = AtomixServer.builder(address, members);
        builder.withStorage(new Storage(StorageLevel.MEMORY)).withTransport(new NettyTransport());
        AtomixServer server = builder.build();
        server.open();
        CompletableFuture<AtomixServer> future = server.open();
        future.join();

    }
}
