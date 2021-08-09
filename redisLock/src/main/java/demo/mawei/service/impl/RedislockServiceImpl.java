package demo.mawei.service.impl;

import demo.mawei.interceptor.RedisLock;
import demo.mawei.service.RedislockService;
import demo.mawei.target.CacheLock;
import demo.mawei.target.LockedObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class RedislockServiceImpl implements RedislockService {

    public Jedis jedis;


    @PostConstruct
    public void init(){
        this.jedis = new Jedis("1.117.215.215",6379);
        this.jedis.auth("root");
    }

    public String get(String key) throws InterruptedException {
        String result = jedis.get(key);
        log.info("获取redis中的内容");
        if(result == null){
            RedisLock lock = new RedisLock("TEST_PREFIX", key);
            try {
                if(lock.lock(1,5000)){
                    //模拟数据库查询
                    result = "DbResult";
                    jedis.set(key,result);
                }else{
                    //资源被锁
                    System.out.println("被锁，睡眠0.1s再次获取锁");
                    //睡眠100ms
                    Thread.sleep(100);
                    result = get(key);
                }
            }finally {
                //一定要释放锁
                lock.unlock();
            }

        }
        return result;
    }

    @Override
    public void lockmethod(String  arg2) {

    }


    public void set(String key,String value){
        Jedis jedis = new Jedis("1.117.215.215",6379);
        jedis.auth("root");
        jedis.set(key,value);
    }
}
