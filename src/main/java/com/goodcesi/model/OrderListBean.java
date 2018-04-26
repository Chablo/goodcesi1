/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.domain.Order;
import com.goodcesi.business.ordermgmt.CallerContext;
import com.goodcesi.business.ordermgmt.OrderManagerLocal;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.*;

/**
 *
 * @author asbriglio
 * invocable que dans le contexte d'une requête demandant une ressource du dossier sécurisé seller ou buyer.
 * sinon une exception est levée.
 * 
 * La liste des commandes retournée dépend de l'emplacement au sein duquel ce bean est invoqué.
 * Cette gestion de liste se basant sur l'emplacement est due au fait qu'un utilisateur peut être vendeur et acheteur.
 * Si le bean est invoqué, par ex, depuis "l'espace vendeur" alors les commandes du vendeurs sont retournées.
 * autrement-dit le bean donne accès à la liste des commandes en tant que vendeur ou acheteur.
 * 
 */
@Named("orderListModel")
public class OrderListBean {
    
    @Inject
    private CurrentUser authenticatedUser;
    
    @Inject OrderManagerLocal orderManager;
    
    private List<Order> orders;
    
    @PostConstruct
    void init(){
        
        CallerContext ctx = null;
        
        //si la requête en cours contient la chaine seller càd invoque une page contenue dans le dossier seller
        if(isInSecuredFolder("seller")){
           //alors l'appelant/l'utilisateur fait une invocation en tant que vendeur
            ctx = CallerContext.AS_SELLER;
        }else if(isInSecuredFolder("buyer")){
            ctx = CallerContext.AS_BUYER;    
        }else{
            //sinon si le bean n'est pas invoqué depuis une ressource contenu dans les espaces sécurisés
            //seller ou buyer alors on lève une exception.
            throw new RuntimeException("impossible d'initialiser le modèle");
        }
        
            //rien n'empêche d'injecter un session bean dans une méthode PostContruct d'une instance de bean CDI
            //il n'y a pas de restriction comme pour la spécification EJB
            orders = orderManager.retrieveOrders(authenticatedUser.getUser(),ctx);
           
     }  

    public List<Order> getOrders() {
        return orders;
    }
    
    public void markOrderAsSent(ValueChangeEvent vce){
        boolean isChecked = (boolean) vce.getNewValue();
        if(isChecked){
          Long orderId= (Long) vce.getComponent().getAttributes().get("orderId");
          Order updatedOrder = orderManager.putOrderIntoSentState(orderId);
          if(orders.contains(updatedOrder)){
              int index = orders.indexOf(updatedOrder);
              orders.remove(updatedOrder);
              orders.add(index, updatedOrder);
              
          }
        }
           //on met à jour la liste - il y a chargement depuis le cache L2
//          orders = orderManager.retrieveOrders(authenticatedUser.getUser(), CallerContext.AS_SELLER);
          
    }
    
    
    private boolean isInSecuredFolder(String securedFolder){
        /*
        technique 2 : on récupère l'id de la vue - on a pas besoin de faces-redirect=true pour naviguer vers orderList
        */
        String idRoot=FacesContext.getCurrentInstance().getViewRoot().getViewId();
       
        boolean contains = idRoot.contains(securedFolder);//code Java 8
        return contains;
    }
}
