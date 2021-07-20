package demo.mawei.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisFactory {

    public static JedisPoolConfig getPoolConfig() throws IOException{
        Properties properties = new Properties();

        InputStream in = RedisFactory.class.getClassLoader().getResourceAsStream("redis.properties");

        try {
            properties.load(in);
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle", "100")));
            config.setMinIdle(Integer.parseInt(properties.getProperty("minIdle", "1")));
            config.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal", "1000")));
            return config;
        } finally {
            in.close();
        }

    }

    public static RedisClient getDefaultClient(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();

        poolConfig.setMaxTotal(10000);// 最大连接数，连接全部用完，进行等待
        poolConfig.setMinIdle(10); // 最小空余数
        poolConfig.setMaxIdle(1000); // 最大空余数
        JedisPool pool = new JedisPool(poolConfig,"1.117.215.215",6379,60000,"root");
        RedisClient client = new RedisClient(pool);
        return client;
    }
}