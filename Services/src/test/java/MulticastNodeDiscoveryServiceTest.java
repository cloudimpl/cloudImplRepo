import com.cloudimpl.core.NotificationChannel;
import com.cloudimpl.services.NodeDiscoveryService;
import com.cloudimpl.services.impl.MulticastNodeDiscoveryService;

import static java.lang.Thread.sleep;

/**
 * Created by Nuwan on 12/2/2015.
 */
public class MulticastNodeDiscoveryServiceTest {

    @org.junit.Test
    public void basicTest() throws Exception {
        NodeDiscoveryService service1 = new MulticastNodeDiscoveryService("224.0.0.0", 4446, new NotificationChannel() {

            @Override
            public void notify(Object notify) throws Exception {
                System.out.println("service1 "+notify);
            }
        });

        NodeDiscoveryService service2 = new MulticastNodeDiscoveryService("224.0.0.0", 4446, new NotificationChannel() {
            @Override
            public void notify(Object notify) throws Exception {
                System.out.println("service2 "+notify);
            }
        });

        service1.start();
        service2.start();

        service1.getPublisher().send("192.168.1.4", 1234);
        service2.getPublisher().send("192.168.1.5", 1235);
        sleep(10000);
    }


}