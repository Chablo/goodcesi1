/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.goodcesi.integration.dao;


import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.TransactionScoped;

/**
 *
 * @author cesi
 * Chargé des opérations de persistance, modification, supression et recherche d'une entité (des données)
 * 
 * [mode CDI]Ce bean a un cycle de vie liée à un contexte transactionnel. Cela signifie que pour chaque 
 * transaction, il y a une instance du bean associée.
 */

@TransactionScoped //scope CDI étendue par la spécification Java des transactions (JTA)
public class CrudService implements CrudServiceLocal, Serializable {
    
    @PersistenceContext(unitName = "goodcesiPU")//unitName facultatif
    private EntityManager em;
    
    //sauvegarde d'un objet
    @Override
    public <T> T create(T t) {
        em.persist(t);
        return t;
    }
    /*
    Recherche d'un objet sauvegardé en base
    */
    @Override
    public <T> T find(Class<T> type, Long id) {
        return em.find(type, id);
    }
    
    //modification de l'état d'un objet en base.
    @Override
    public <T> T update(T t) {
        t = em.merge(t);
        return t;
    }
    //suppression en base de l'enregistrement (ou les enregistrements) correspondant à un objet
    @Override
    public void delete(Object t) {
        t = em.merge(t);
        em.remove(t);
    }
    /*
    Récupération de l'ensemble  des objets qui ont été persistés en base pour un type donné.
    */
    @Override
    public <T> List<T> findAll(Class<T> type){
        //utilisation de la généricité quasi impossible avec JPQL
        //on comprend l'un des avantages d'une API "typée" (criteria) pour exécuter des requêtes JPA
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(type);
        Root<T> identificationVariable = cq.from(type);
        cq.select(identificationVariable);
        TypedQuery<T> query = em.createQuery(cq);
        return query.getResultList();
    }
    
}
