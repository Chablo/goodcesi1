/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.ordermgmt;

import com.goodcesi.business.domain.Order;
import com.goodcesi.business.domain.User;
import java.util.List;

/**
 *
 * @author asbriglio
 */

public interface OrderManagerLocal {

    public void createOrder(Order order);
    
    public List<Order> retrieveOrders(User user, CallerContext context);

    public Order putOrderIntoSentState(Long orderId);
    
}
