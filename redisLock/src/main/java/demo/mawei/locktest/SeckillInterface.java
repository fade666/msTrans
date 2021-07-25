package demo.mawei.locktest;

import demo.mawei.target.CacheLock;
import demo.mawei.target.LockedObject;

public interface SeckillInterface {
    @CacheLock(lockedPrefix="TEST_PREFIX")
    public void secKill(String arg1,@LockedObject Long arg2);
}
