import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final JedisCluster jedisCluster;

    static {
        synchronized (Main.class) {
            Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
            jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6371));
            jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6372));
            jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6373));
            jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6374));
            jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6375));
            jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6376));
            jedisCluster = new JedisCluster(jedisClusterNodes, null, "bitnami");
        }
    }

    private static void runStats() {
        for (String key : Arrays.asList("192.168.32.2:6379", "192.168.32.3:6379", "192.168.32.4:6379",
                "192.168.32.5:6379", "192.168.32.6:6379", "192.168.32.7:6379")) {
            System.out.println("key : " + key + " ***");
            System.out.println(" max-total           : " + jedisCluster.getClusterNodes().get(key).getMaxTotal());
            System.out.println(" active              : " + jedisCluster.getClusterNodes().get(key).getNumActive());
            System.out.println(" mean-active-ms      : " + jedisCluster.getClusterNodes().get(key).getMeanActiveTimeMillis());
            System.out.println(" borrow              : " + jedisCluster.getClusterNodes().get(key).getBorrowedCount());
            System.out.println(" mean-borrow-wait-ms : " + jedisCluster.getClusterNodes().get(key).getMeanBorrowWaitTimeMillis());
            System.out.println(" max-wait-ms         : " + jedisCluster.getClusterNodes().get(key).getMaxWaitMillis());
        }
    }

    private static void generateLoad(int amount) {
        for (int i = 0; i < amount; i++) {
            jedisCluster.set("x:x" + i, "value_x_" + i);
        }
    }

    public static void main(String args[]) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            generateLoad(100);
            System.out.println(jedisCluster.get("x:x1"));
            runStats();
        }, 0, 5, TimeUnit.SECONDS);
    }
}
