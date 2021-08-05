package demo.mawei.service;

import demo.mawei.target.CacheLock;
import demo.mawei.target.LockedObject;

public interface RedislockService {
    @CacheLock(lockedPrefix="TEST_PREFIX")
    public void lockmethod(@LockedObject String arg2);
}
