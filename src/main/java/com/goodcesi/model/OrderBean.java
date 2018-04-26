/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.domain.*;
import com.goodcesi.business.ordermgmt.OrderManagerLocal;
import java.io.Serializable;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asbriglio
 * bean gérant la commande. C'est un processus critique notamment en ce qui concerne la concurrence et la sécurité.
 * On utilise donc un EJB Stateful.
 * 
 * A noter que dans notre cas le comportement transactionnel de ce composant n'est pas critique car 
 * c'est la couche logique métier implémentée par les session beans stateless qui est chargée
 * de piloter la gestion des transactions.
 * 
 * Le stateful bean sera détruit automatiquement lorsque la conversation se termine.
 * 
 * Si on annote avec @ConversationScoped (ou autre annotation de scope) alors il ne faut pas annoter de méthodes avec @Remove car c'est le container CDI qui est chargé de gérer le cycle de vie du bean.
 * A l'exécution chaque instance du session bean sera associée à une instance contextuelle CDI dont le scope est la session utilisateur. 
 * Pour simplifier, considérez que l'instance du session bean est l'instance contextuelle. Le cycle de vie du session bean
 * est donc sous contrôle du container CDI qui est chargée de gérer les instances. Lorsque la session utilisateur se termine le container CDI instruira la destruction
 * de l'instance contextuelle. C'est pour cela que le client  ne doit pas demandée explicitement la destruction du session bean.
 * Par conséquent il ne faut pas annoter de méthodes, par exemple cancel ou process, avec @Remove. 
 * @Remove indique au container EJB l'instance de l'EJB Stateful doit être détruite à la fin de l'exécution de la méthode.
 * Si le Session Bean Stateful n'est pas associé à un scope CDI, alors il faut des méthodes @Remove pour permettre la destruction de l'instance.
 */
@Named("orderModel")
@SessionScoped
public class OrderBean implements Serializable {//un SFSB n'a pas besoin d'implémenter Serializable pour activer la capacité de passivation
    
    private Item orderedItem;  
    private String ccNumber; 
    private Order order;
    
    
    @Inject 
    private CurrentUser currentUser;
    
   
    @Inject
    private OrderManagerLocal orderManager;
    
    //depuis JSF 2 les méthodes d'action acceptent des paramètres.
    public String beginOrdering(Item i){
        //si la conversation en cours n'est pas une conversation de longue durée
        // promotion en conversation longue durée
       
        this.orderedItem = i;
        //on crée une commande
        order=new Order(); //status In Progress
        order.setOrderedItem(orderedItem);//on assigne l'article à la commande en cours - rappel relation 1-1 undirectionnelle de Order vers Item
          
        return "buyer/orderInfo?faces-redirect=true";
    }
    
    public String checkOrder(){ 
        //l'instance de CurrentUser liée à la session embarquant l'entité représentant l'utilisateur authentifié est créée par le producteur lors de la première utilisation. 
        //Donc l'instance est créée pour l'invocation ci-dessous.
        
       User buyer = currentUser.getUser();//on récupère l'entité JPA correspondant à l'utilisateur authentifié
        //si le vendeur EST l'acheteur
        if(buyer.getId().equals(orderedItem.getSeller().getId())){
            //alors il est interdit de commander
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Interdiction de commander.","Vous ne pouvez pas commander un article que vous vendez"));//un message est ajouté
            return null;
        }
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setTotalPrice(orderedItem.getPrice()+orderedItem.getShippingCharge());
       //on ne met pas le préfixe "buyer" vu que la page est invoquée depuis une page contenue dans le dossier buyer
        return "orderValidation";
    }
    
    public String pay(){
        if(ccNumber.length()==10){
            User buyer = currentUser.getUser();//on récupère l'entité JPA correspondant à l'utilisateur authentifié
           
            buyer.add(order);//on ajoute la commande à la liste des commande de cet acheteur.

            //Order est le côté propriétaire de la relation, il faut établir la relation avec acheteur sinon la relation ne sera pas persistée
            order.setBuyer(buyer);// on établit la relation entre order et l'acheteur. 

            orderedItem.setStatus(ItemStatus.BUYED);//on marque l'item vendu
            
            //on passe la commande en mode VALIDATED
            order.setStatus(OrderStatus.VALIDATED);

            //on met à jour la base
            orderManager.createOrder(order);
            
           //Si la conversation est de longue durée
           //destruction du bean en fin d'exécution de la méthode
          
            return "orderSummary";
        } else {
            return "paymentError";
        }       
    }

    public Item getOrderedItem() {
        return orderedItem;
    }

    public CurrentUser getCurrentUser() {
        return currentUser;
    }
    
    public Order getOrder() {
        return order;
    }

    
    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCcNumber() {
        return ccNumber;
    }
    
    
       
}
