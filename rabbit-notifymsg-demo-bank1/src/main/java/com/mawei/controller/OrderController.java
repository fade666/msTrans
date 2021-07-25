package com.mawei.controller;

import com.mawei.entity.pojo.Order;
import com.mawei.entity.vo.R;
import com.mawei.service.DictFeignClient;
import com.mawei.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 订单接口管理
 *
 * @author xiejianwei
 */
@RestController
@RequestMapping("/order")
@Validated
public class OrderController {

    @Autowired
    public OrderService orderService;

    @Autowired
    public DictFeignClient dictFeignClient;

    @PostMapping("/start")
    public R page(@RequestBody String productName) {
        Order order = new Order();
        order.setAmount(BigDecimal.valueOf(5000));
        order.setProductName(productName);
        order.setOrderId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        orderService.start(order);
        return R.success();
    }

    /**
     * 模拟远程调用返回超时的情况
     * @return String
     * @throws InterruptedException
     */
    @GetMapping("/delay")
    @HystrixCommand(defaultFallback = "delayFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000") // 设置超时时间为3秒，超过3秒的由降级方法返回提示
    })
    public String delay() throws InterruptedException {
        float s = 5.1F;  // 这个时间肯定会被降级（即：返回的内容是delayFallback的返回值），可以改小点看结果。
        Thread.sleep((long)(s * 1000));
        return "延迟" + s + "秒， 访问正常";
    }

    /**
     * 降级方法
     * @return String
     */
    public String delayFallback() {
        return "执行超时了，被降级...";
    }

    // fallbackMethod: 设置HystrixCommand服务降级所使用的方法名称，注意该方法需要与原方法定义在同一个类中，并且方法签名也要一致
    // defaultFallback: 设置HystrixCommand默认的服务降级处理方法，如果同时设定了fallbackMethod，会优先使用fallbackMethod所指定的方法，
    // 需要注意的是defaultFallback该属性所指定的方法没有参数，需要注意返回值与原方法返回值的兼容性
    @GetMapping("/sleepDelay/{id}")
    @HystrixCommand(fallbackMethod = "sleepFallbackMethod", defaultFallback = "sleepFallback") // 使用指定的降级方法
    public String sleepDelay(@PathVariable("id") int id){
        return dictFeignClient.sleep();
    }

    // 参数签名必须与原方法相同
    public String sleepFallbackMethod(int id) {
        return "sleep 方法被 控制器中指定的方法sleepFallbackMethod 降级，参数是：" + id;
    }

    // 可以造成熔断的方法
    @GetMapping("/sleep5/{id}")
    @HystrixCommand(defaultFallback = "sleepBreaker", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),  // 开启熔断器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),  // 当请求达到这个数量之后，才进行错误占比的计算。
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),  // 半打开休眠时间，熔断之后过了这段休眠时间，就会半打开，尝试接口是否恢复，如果恢复就完全打开熔断器。
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")  // 错误占比，当错误次数超过这个百分比，就会熔断。
    }) // commandProperties 的整个意思就是10秒中10次请求中有60%的请求出问题就熔断
    public String sleep5(@PathVariable("id") int id) throws Exception {
        if (id < 0) {
            throw new Exception("id 是负数，抛出异常！！");
        }
        return "访问正常 id:";
    }

    // 熔断时调用的方法
    public String sleepBreaker() {
        return "接口有问题，目前被熔断了...";
    }
}
