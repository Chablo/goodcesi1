/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.catalogmgmt.CatalogManagerLocal;
import com.goodcesi.business.domain.Category;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asbriglio
 * Cache applicatif chargeant le menu des catégories.
 * les catégories varient peu au fil du temps, on peut donc les cacher
 * 
 * Ce sesion bean ne doit jamais être invoquée dans un contexte transactionnel
 */
@Named
@RequestScoped
public class CategoriesCache {
    
    private  List<Category> categories;
    
    @Inject CatalogManagerLocal catalogManager;

    public CategoriesCache() {}
    
    @PostConstruct 
    void init(){
        //chargement des catégories existantes au démarrage de l'appli
        categories = catalogManager.getAllCategories();
    }
    
    public List<Category> getCategories() {
        return categories;
    }
         
}
