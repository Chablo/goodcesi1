/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author asbriglio
 */
@Entity
public class UserGroup implements Serializable {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    private String description;
    
    //relations
    @ManyToMany(mappedBy = "groups") //User.groups : côté propriétaire de la relation
    private Set<User> users = new HashSet<>();
    
    public UserGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UserGroup() {//ou protected
    }
        
    public void add(User user){//surcharge
        users.add(user);
    }
    
    public Set<User> getUsers() {
        return users;
    }
    
    
    

    public Long getId() {
        return id;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.name);
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
        final UserGroup other = (UserGroup) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) { //java.util.Objects
            return false;
        }
        return true;
    }
    
    
    
    
}
