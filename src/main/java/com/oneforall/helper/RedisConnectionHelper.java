package com.oneforall.helper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnectionHelper {
    public static void main(String[] args) {
        // Set up pool configuration
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);

        // Create Jedis pool with authentication
        JedisPool jedisPool = new JedisPool(poolConfig, "host", 6379, 5000, "username", "password");

        // Get Jedis object from pool
        try (Jedis jedis = jedisPool.getResource()) {
            // Perform Redis operations
            jedis.set("mykey", "myvalue");
            String value = jedis.get("mykey");
            System.out.println("Value of mykey: " + value);
        }

        // Close Jedis pool
        jedisPool.close();
    }
}