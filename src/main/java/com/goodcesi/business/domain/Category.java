/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author asbriglio
 * les catégories ne sont pas mises en cache côté JPA car elles sont cachées dans un cache applicatif
 */
@Cacheable(false)
@Entity
public class Category implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
     
    private String title;
    
    //relation
    @ManyToMany(mappedBy = "categories")//entité proprio : Item / côté propriétaire Item.categories
    private List<Item> items = new ArrayList<>();

    public Category(String title) {
        this.title = title;
    }

    protected Category() {}
    
    public void add(Item item){
        items.add(item);
    }
    
    public List<Item> getItems() {
        return items;
    }
    
    

    public Long getId() {
        return id;
    }
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
    
}
