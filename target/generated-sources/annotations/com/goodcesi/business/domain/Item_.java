package com.goodcesi.business.domain;

import com.goodcesi.business.domain.Category;
import com.goodcesi.business.domain.ItemStatus;
import com.goodcesi.business.domain.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-11T17:26:20")
@StaticMetamodel(Item.class)
public class Item_ { 

    public static volatile SingularAttribute<Item, User> seller;
    public static volatile SingularAttribute<Item, Double> price;
    public static volatile SingularAttribute<Item, String> name;
    public static volatile SingularAttribute<Item, String> description;
    public static volatile SingularAttribute<Item, Double> weight;
    public static volatile SingularAttribute<Item, Long> id;
    public static volatile SingularAttribute<Item, Double> shippingCharge;
    public static volatile ListAttribute<Item, Category> categories;
    public static volatile SingularAttribute<Item, Date> publicationDate;
    public static volatile SingularAttribute<Item, Long> version;
    public static volatile SingularAttribute<Item, ItemStatus> status;

}