package com.cloudimpl.vm;

import io.atomix.Atomix;
import io.atomix.AtomixClient;
import io.atomix.AtomixReplica;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.coordination.DistributedLock;
import io.atomix.coordination.DistributedMembershipGroup;
import io.atomix.copycat.server.storage.Storage;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by Nuwan on 11/30/2015.
 */
public class App2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, UnknownHostException {
//        List<Address> members = Arrays.asList(
//                new Address("127.0.0.1", 5000)
//        );
//
//        AtomixClient client = (AtomixClient) AtomixClient.builder(members)
//                .withTransport(new NettyTransport())
//                .build();
//
//        client.open().get();
//
//        client.open().thenRun(() -> {
//            System.out.println("Client started!");
//        });
//
//        DistributedLock lock = client.create("lock", DistributedLock::new).get();
        int port = 5000;
        Address address = new Address(InetAddress.getLocalHost().getHostName(), port);

        List<Address> members = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            String[] parts = args[i].split(":");
            members.add(new Address(parts[0], Integer.valueOf(parts[1])));
        }

        Atomix atomix = AtomixReplica.builder(address, members)
                .withTransport(new NettyTransport())
                .withStorage(Storage.builder()
                        .withDirectory(System.getProperty("user.dir") + "/logs/" + UUID.randomUUID().toString())
                        .build())
                .build();

        atomix.open().join();

        System.out.println("Creating membership group");
        DistributedMembershipGroup group = atomix.create("group", DistributedMembershipGroup.class).get();

        System.out.println("Joining membership group");
        group.join().thenAccept(member -> {
            System.out.println("Joined group with member ID: " + member.id());
        });

        group.onJoin(member -> {
            System.out.println(member.id() + " joined the group!");

            long id = member.id();
            member.execute((Serializable & Runnable) () -> System.out.println("Executing on member " + id));
        });

        while (atomix.isOpen()) {
            Thread.sleep(1000);
        }
    }
}
