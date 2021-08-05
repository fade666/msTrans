package demo.mawei.controller;
import demo.mawei.service.impl.RedislockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lock")
public class RedisLockTest {

    @Autowired
    private RedislockServiceImpl redislockService;

    @GetMapping("/test/{key}")
    public String test(@PathVariable(value = "key")String key) throws InterruptedException {
        String result = redislockService.get(key);
        return result;
    }

}
