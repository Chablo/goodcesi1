/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.ordermgmt;

import com.goodcesi.business.domain.Item;
import com.goodcesi.business.domain.Order;
import com.goodcesi.business.domain.OrderStatus;
import com.goodcesi.business.domain.User;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.goodcesi.integration.dao.CrudServiceLocal;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;

/**
 *
 * @author asbriglio
 * 
 * Composant chargé d'implémenter la gestion d'une commande
 * 
 */
@Dependent
@Transactional
public class OrderManager implements OrderManagerLocal, Serializable {

    @PersistenceContext
    private EntityManager em;
      
    @Inject
    private CrudServiceLocal dao;
    
    //création d'une commande entrainant la mise à jour de la base de données.
    @Override
    public void createOrder(Order order){
      dao.create(order);
      Item i = order.getOrderedItem();//i n'est pas managé car pas de cascade sur la relation.
      dao.update(i);//mise à jour de l'item (pour que le statut soit modifié)-l'item est désormais managé - la relation avec l'acheteur sera persistée.
     
      //ne pas utiliser l'API java.lang.Thread dans un environnement Java EE. Ici c'est pour simuler un traitement long et visualiser l'exécution asynchrone 
//      try {
//          System.out.println("démarrage d'un traitement long de paiement");
//            Thread.sleep(10000L);
//            System.out.println("fin du traitement de paiement");
//        } catch (InterruptedException ex) {
//            Logger.getLogger(OrderManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    
    /*
    récupération de la liste des commandes en fonction du contexte dans lequel la méthode
    est invoquée. invocation depuis un espace "vendeur" ou "utilisateur"
    
    on redéfine le comportement transactionnel défini pour le bean
    Cette méthode ne s'exécutera qu'au sein d'une transaction déjà existante.
    */
    @Override
    public List<Order> retrieveOrders(User user,CallerContext context) {
      //l'entité passée n'a pas besoin d'être managé car en coulisse la requête SQL généré se basera sur la clé primaire 
      String jpql =null;
      switch(context){
          case AS_SELLER :
              jpql = "SELECT o From Order o JOIN o.orderedItem i WHERE i.seller=:user";
              break;
          case AS_BUYER :
              jpql = "SELECT o From User u JOIN u.orders o WHERE u=:user";//à tester
              break;    
      }
      
      List<Order> orders = em.createQuery(jpql, Order.class).setParameter("user", user).getResultList();
      return orders;
    }
    
    //passer la commande en état "envoyé"
    @Override
    public Order putOrderIntoSentState(Long orderId){
        Order order = dao.find(Order.class, orderId); //order est dans un contexte de persistance associé à une transaction et est donc managé
        order.setStatus(OrderStatus.SENT);//changement sauvegardé en base à la fin de la transaction.
        order.setShippingDate(new Date()); //on assigne la date d'envoi
        return order;
    }
        
}
