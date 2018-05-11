package com.goodcesi.business.domain;

import com.goodcesi.business.domain.Item;
import com.goodcesi.business.domain.OrderStatus;
import com.goodcesi.business.domain.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-11T17:26:20")
@StaticMetamodel(Order.class)
public class Order_ { 

    public static volatile SingularAttribute<Order, Item> orderedItem;
    public static volatile SingularAttribute<Order, Date> shippingDate;
    public static volatile SingularAttribute<Order, Double> totalPrice;
    public static volatile SingularAttribute<Order, Date> orderDate;
    public static volatile SingularAttribute<Order, OrderStatus> status;
    public static volatile SingularAttribute<Order, User> buyer;

}