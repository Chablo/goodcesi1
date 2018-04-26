/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.catalogmgmt.CatalogManagerLocal;
import com.goodcesi.business.domain.*;
import com.goodcesi.qualifier.ScopeMonitor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asbriglio
 * Bean chargé de la création d'un nouvel article
 */
@Named("itemModel")
@ConversationScoped
@ScopeMonitor
public class ItemBean implements Serializable{
    @Inject
    private CatalogManagerLocal catalogManager;
    
    @Inject CurrentUser currentUser;
    
    @Inject Conversation conversation;
    
    private Item item;
    
    private Set<Long> categoriesId = new HashSet<>();
    
    @PostConstruct
    void init(){
        item =new Item();
       /*on aurait pu promouvoir une conversation longue durée depuis cette méthode PostConstruct,
        si le bean n'intervenait pas aussi dans des opérations de scope request (ou de conversation transiente)
       */ 
    }
    
    public String initConversation(){
        
        /*
        Par défaut un bean avec un scope de conversation est transient (vie le temps de la requête)
        conversation.begin() promeut l'instance en tant que bean de longue durée. l'instance sera associée 
        au scope de conversation jusqu'à conversation.end() invoquée. end() indique au container CDI que lorsque
        la méthode ayant invoquée end() se termine l'instance doit être détruite.
        */
        if(conversation.isTransient()){
            conversation.begin();//le bean a désormais un scope s'étendant au delà de la requête
            System.out.println("démarrage de la conversation d'id "+conversation.getId());
        }
        
        return "/seller/itemCreation";
    }
    
    public void linkToCategory(ValueChangeEvent ve){
        if(((boolean) ve.getNewValue())==true) {
          Long catId=(Long) ve.getComponent().getAttributes().get("catId");//les attributs sont associés aux events 
          categoriesId.add(catId);       
        }
    }
    
    public String createNewItem(){
       //currentUser est créé lors de sa première utilisation dans la session. 
       //CurrentUserFactory prodruit donc l'instance CurrentUser lors de sa première utilisation dans la session
       User user = currentUser.getUser();
       
      //item a ses propriétés assignées dans la vue
      //on réassigne le retour de la méthode à item pour s'assurer de la persistance de l'item. 
      //Si l'article n'a pas été correctement renseigné null est retourné - implémentation perfectible
       item = catalogManager.saveNewItem(item,user,categoriesId); //les paramètres des méthodes métiers exposées via une vue locale sont passés par référence
       
       if(!conversation.isTransient()) {//si la conversation est de longue durée
            System.out.println("FIN de la conversation d'id "+conversation.getId());
            conversation.end();//la conversation se termine.
       }     
        return "/index";
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Set<Long> getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(Set<Long> categoriesId) {
        this.categoriesId = categoriesId;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }
    
}
