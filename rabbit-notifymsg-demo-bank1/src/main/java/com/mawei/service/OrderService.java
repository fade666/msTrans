package com.mawei.service;

import com.mawei.entity.pojo.Order;

/**
 * @author xiejianwei
 */
public interface OrderService {

    /**
     * 模拟发起一个订单
     *
     * @param order
     * @return
     */
    boolean start(Order order);

}
