/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.catalogmgmt.CatalogManager;
import com.goodcesi.business.catalogmgmt.CatalogManagerLocal;
import com.goodcesi.business.domain.*;
import java.util.*;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author Asbriglio
 * bean chargé du parcours dans le catalogue
 */
@Named("catalogModel")
@RequestScoped
public class CatalogBean{
    
    private CatalogManagerLocal catalogManager = new CatalogManager();
    
    private List<Item> itemsFromCategory;
    
    private Long currentItemId;
    
    private Item currentItem;
    
    public String retrieveItemsFromCategory(){
        //récupération d'une Map contenant les paramètres associés à la requête
       Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
       //récupération de la valeur du paramètre nommé catId
       Long id =  Long.parseLong(params.get("catId"));
        //récupération de la liste des articles associés à une catégorie
       itemsFromCategory=catalogManager.getItemsByCatId(id);
        return "/itemList";//navigation vers la vue itemList
    }
    

    public String loadCurrentItem(){
        if(currentItemId!=null) 
          currentItem = catalogManager.getItemById(currentItemId);
        
        return null;
    }
    
    public List<Item> getItemsFromCategory() {
        return itemsFromCategory;
    }
    
 
    public Long getCurrentItemId() {
        return currentItemId;
    }

    
    public void setCurrentItemId(Long currentItemId) {
        this.currentItemId = currentItemId;
    }

    
    public Item getCurrentItem() { 
        return currentItem;
    } 
    
}
