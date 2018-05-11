/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.domain.UserGroup;
import com.goodcesi.business.usermgmt.UserManagerLocal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Asbriglio
 * bean chargeant la liste des groupes qu'un utilisateur peut sélectionner lors de son inscription.
 */
@RequestScoped
@Named("groupsModel")
public class GroupsBean {
    
    @Inject private UserManagerLocal userManager;
    
    //String mappée avec le label d'un item de la liste -Long mappé avec la valeur d'un item de la liste
    private final Map<String, Long> availableGroups =new HashMap<>(); 
     
    @PostConstruct
     void init(){
       // on charge les groupes disponible pour l'inscription 
       List<UserGroup> userGroups = userManager.getAllAvailableCustomerGroup();
       for(UserGroup ug :userGroups){
          availableGroups.put(ug.getName(), ug.getId());
       }
       
     }

    public Map<String,Long> getAvailableGroups() {
        return availableGroups;
    }
          
}
