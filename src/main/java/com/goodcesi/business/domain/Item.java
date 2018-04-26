/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
/**
 *
 * @author asbriglio
 */
@Entity
public class Item implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private Double price;
    private Double weight;
    private Double shippingCharge;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicationDate;
    
    @Enumerated(EnumType.ORDINAL)
    private ItemStatus status;
    
    //gestion des accès concurrents via verrouillage optimiste - permet d'éviter que des transactions modifient simultanément un article
    //exemple de modification concurrente : un vendeur modifie le prix pendant que l'article est acheté (changement de statut)
    @Version
    private Long version; 
    
    //relation
    @ManyToOne
    private User seller; //côté propriétaire
    
    @ManyToMany
    private List<Category> categories = new ArrayList<>();

    public Item(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        
    }

    public Item() {
    }
    
    //méthode écouteur pour l'évènement PrePersist 
    @PrePersist //invoqué par JPA juste avant em.persist donc avant insertion en base
    void beforeInsert(){
        this.status = ItemStatus.FOR_SALE;
        this.publicationDate = new Date(); //date au moment de la sauvegarde
    }
    
    @PostPersist
    void afterInsert(){
        System.out.println("--PostPersist - valeur de version "+version);
        System.out.println("--PostPersist - valeur d'id "+id);
    }

    public void add(Category category){
        categories.add(category);
    }
    
    public List<Category> getCategories() {
        return categories;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }
    
    public User getSeller() {
        return seller;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }
        
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(Double shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    
    public Date getPublicationDate() {
        return publicationDate;
    }

}
