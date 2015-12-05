package com.cloudimpl.vm;

import io.atomix.Atomix;
import io.atomix.AtomixReplica;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.NettyTransport;
import io.atomix.coordination.DistributedMembershipGroup;
import io.atomix.copycat.server.storage.Storage;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Group membership example.
 *
 * @author <a href="http://github.com/kuujo>Jordan Halterman</a>
 */
public class GroupMembershipExample {

    /**
     * Starts the server.
     */
    public static void main(String[] args) throws Exception {
//        if (args.length < 2)
//            throw new IllegalArgumentException("must supply a local port and at least one remote host:port tuple");

        args = new String[2];
        args[0] = ""+5001;
        args[1] = InetAddress.getLocalHost().getHostName()+":"+5000;
        int port = Integer.valueOf(args[0]);
        System.out.println(InetAddress.getLocalHost());
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
