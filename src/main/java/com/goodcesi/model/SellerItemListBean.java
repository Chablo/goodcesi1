/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

//import javax.el.*;
//import javax.faces.context.FacesContext;
//import javax.faces.component.*;
//import javax.faces.component.html.*;

import com.goodcesi.business.catalogmgmt.CatalogManagerLocal;
import com.goodcesi.business.domain.Item;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.inject.*;
import javax.faces.event.*;
import javax.faces.view.ViewScoped;

/**
 *
 * @author asbriglio
 * bean permettant de gérer la liste des commandes d'un vendeur
 */
@Named(value = "itemListModel")
@ViewScoped //instance existant tant qu'on reste sur la vue - une instance par vue visitée.
public class SellerItemListBean implements Serializable{
  //On utilise une map contenant les listes à vendre et vendus  
    private Map<String,List<Item>> items;
    
    private CatalogManagerLocal catalogManager;
    
    
    private CurrentUser currentUser;
    
    private boolean updatable = false;
    
    /**
     * Creates a new instance of SellerItemListBean
     * @param catalogManager
     * @param user
     */
    //injection via constructeur
    @Inject
    public SellerItemListBean(CatalogManagerLocal catalogManager, CurrentUser user) {//les arguments sont des points d'injection
       this.catalogManager=catalogManager;
       this.currentUser = user;
    }
     
    public SellerItemListBean() {
    }
       
    @PostConstruct
    void init(){
        items=catalogManager.getItemsFromSeller(currentUser.getUser().getId());
    }

    public Map<String, List<Item>> getItems() {
        return items;
    }
    
    public void validateUpdates(ActionEvent ae){//depuis JSF 2.2, l'argument ActionEvent n'est plus obligatoire       
      List<Item> its  = items.get("FOR_SALE");//récupération de la liste des items à vendre
      catalogManager.updateItems(its); //mise à jour des items de la liste 
      setUpdatable(false);//on sort du mode édition
    }
    
    public void changeEditableMode(ActionEvent ae){
       updatable =!updatable ;//! opérateur unaire inversant la valeur booléenne
    }
    
    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }
    
}
