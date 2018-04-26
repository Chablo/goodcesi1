/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author asbriglio
 */
@Entity
public class User implements Serializable{
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstname, lastname;
    private String address; //on simplie en déclarant l'adresse en tant que chaîne
    private String email;
    
    //credentiels
    private String login;
    private String password;
    
    //relations
    @ManyToMany
    private Set<UserGroup> groups =new HashSet<>();
    
    @OneToMany(mappedBy = "buyer")
    private List<Order> orders = new ArrayList<>();
    
    @OneToMany(mappedBy = "seller")
    private List<Item> items = new ArrayList<>();;

    protected User() {//ou public    
    }
    
    public User(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    public void add(UserGroup group){
        groups.add(group);
    }
    
    public void add(Order order){//surcharge
        orders.add(order);
    }
    
    public void add(Item item){
        items.add(item);
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Item> getItems() {
        return items;
    }
       
    public Long getId() {
        return id;
    }
      
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.login);
        return hash;
    }
    //pour la consistance de l'égalité, les var d'instance utilisées dans hashCode doivent être obligatoirement utilisées dans equals
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        return true;
    }

    
}
