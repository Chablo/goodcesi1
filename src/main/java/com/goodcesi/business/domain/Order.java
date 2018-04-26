/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author asbriglio
 */
@Entity
@Table(name="ItemOrder")//pour éviter des erreurs de génération SQL avec un nom de table Order par défaut.
public class Order implements Serializable {

    private Double totalPrice;
    
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    
    @Temporal(TemporalType.DATE)
    private Date shippingDate;
    
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;
    
    //relations
    @ManyToOne
    private User buyer;//acheteur
    
    //id dérivée - l'id de Order (entité dépendante) est dérivée de la clé primaire de Item (parent)
    //la colonne clé primaire de la table principale de Order est une clé étrangère référençant la clé primaire  le de la table de Item
    @OneToOne //relation unidirectionnelle
    @Id //l'attribut Id est une relation 1-1 avec l'entité parent Item
    private Item orderedItem; 

    public Order() {
        this.status = OrderStatus.IN_PROGRESS;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Item getOrderedItem() {
        return orderedItem;
    }

    public void setOrderedItem(Item item) {
        this.orderedItem = item;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    /*
    on surdéfinit equals et hascode pour que les entités (de même type) puissent êtres considérées significativement égales
    si elles ont le même id(identité) notamment si on recherche dans une liste.
    A noter que le framework JPA considère égales 2 entitités si elles ont la même identité.
    */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.orderedItem);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (!Objects.equals(this.orderedItem.getId(), other.orderedItem.getId())) {
            return false;
        }
        return true;
    }
    
    
    
    
}
