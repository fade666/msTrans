package demo.mawei.interceptor;

import demo.mawei.target.CacheLock;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author Maw
 * @Date 2021/7/18 22:28
 * @Version 1.0
 */
public class CacheLockInterceptor implements InvocationHandler {
    public static int ERROR_COUNT  = 0;
    private Object proxied;
    public CacheLockInterceptor(Object proxied) {
        this.proxied = proxied;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CacheLock cacheLock = method.getAnnotation(CacheLock.class);
        //没有cacheLock注解，pass
        if(null == cacheLock){
            System.out.println("no cacheLock annotation");
            return method.invoke(proxied, args);
        }
        //获得方法中参数的注解
        Annotation[][] annotations = method.getParameterAnnotations();
        //根据获取到的参数注解和参数列表获得加锁的参数
        Object lockedObject = getLockedObject(annotations,args);
        String objectValue = lockedObject.toString();
        //新建一个锁
        RedisLock lock = new RedisLock(cacheLock.lockedPrefix(), objectValue);
        //加锁
        boolean result = lock.lock(cacheLock.timeOut(), cacheLock.expireTime());
        if(!result){//取锁失败
            ERROR_COUNT += 1;
            throw new CacheLockException("get lock fail");
        }
        try{
            //加锁成功，执行方法
            return method.invoke(proxied, args);
        }finally{
            lock.unlock();//释放锁
        }
    }
    /**
     *
     * @param annotations
     * @param args
     * @return
     * @throws CacheLockException
     */
    private Object getLockedObject(Annotation[][] annotations,Object[] args) throws CacheLockException{
        if(null == args || args.length == 0){
            throw new CacheLockException("方法参数为空，没有被锁定的对象");
        }
        if(null == annotations || annotations.length == 0){
            throw new CacheLockException("没有被注解的参数");
        }
        //不支持多个参数加锁，只支持第一个注解为lockedObject或者lockedComplexObject的参数
        int index = -1;//标记参数的位置指针
        for(int i = 0;i < annotations.length;i++){
            for(int j = 0;j < annotations[i].length;j++){
                if(annotations[i][j] instanceof LockedComplexObject){//注解为LockedComplexObject
                    index = i;
                    try {
                        return args[i].getClass().getField(((LockedComplexObject)annotations[i][j]).field());
                    } catch (NoSuchFieldException | SecurityException e) {
                        throw new CacheLockException("注解对象中没有该属性" + ((LockedComplexObject)annotations[i][j]).field());
                    }
                }
                if(annotations[i][j] instanceof LockedObject){
                    index = i;
                    break;
                }
            }
            //找到第一个后直接break，不支持多参数加锁
            if(index != -1){
                break;
            }
        }
        if(index == -1){
            throw new CacheLockException("请指定被锁定参数");
        }
        return args[index];
    }

    /**
     * 加锁
     * 使用方式为：
     * lock();
     * try{
     *    executeMethod();
     * }finally{
     *   unlock();
     * }
     * @param timeout timeout的时间范围内轮询锁
     * @param expire 设置锁超时时间
     * @return 成功 or 失败
     */
    public boolean lock(long timeout,int expire){
        long nanoTime = System.nanoTime();
        timeout *= MILLI_NANO_TIME;
        try {
            //在timeout的时间范围内不断轮询锁
            while (System.nanoTime() - nanoTime < timeout) {
                //锁不存在的话，设置锁并设置锁过期时间，即加锁
                if (this.redisClient.setnx(this.key, LOCKED) == 1) {
                    this.redisClient.expire(key, expire);//设置锁过期时间是为了在没有释放
                    //锁的情况下锁过期后消失，不会造成永久阻塞
                    this.lock = true;
                    return this.lock;
                }
                System.out.println("出现锁等待");
                //短暂休眠，避免可能的活锁
                Thread.sleep(3, RANDOM.nextInt(30));
            }
        } catch (Exception e) {
            throw new RuntimeException("locking error",e);
        }
        return false;
    }
    public  void unlock() {
        try {
            if(this.lock){
                redisClient.delKey(key);//直接删除
            }
        } catch (Throwable e) {
        }
    }
}
