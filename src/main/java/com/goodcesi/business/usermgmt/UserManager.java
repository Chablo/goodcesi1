/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.usermgmt;

import com.goodcesi.business.domain.User;
import com.goodcesi.business.domain.UserGroup;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import com.goodcesi.integration.dao.CrudServiceLocal;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;

/**
 *
 * @author Asbriglio
 * En charge de la gestion des comptes utilisateur
 */
@Dependent
@Transactional
public class UserManager implements UserManagerLocal, Serializable {

    @Inject
    private CrudServiceLocal dao;
    
    @PersistenceContext
    private EntityManager em;
    

    @Override
    //sauvegarde du compte utilisateur associé à ses groupes.
    public boolean create(User u, List<Long> groupIds) {
       if (u!=null){
           dao.create(u);
           for (Long gid : groupIds){
            UserGroup group = dao.find(UserGroup.class, gid);
            u.add(group);
            //Pour respecter le modèle du domaine qui décrit une relation M-M bidirectionnelle entre User et UserGroup, on établit la relation inverse Group->User
            //même si pour persister la relation ce n'est pas obligatoire car l'entité User est propriétaire de la relation
            group.add(u);
           }
           return u.getGroups().size() >= 1; //true si l'utilisateur est au moins associé à un groupe
           
       }
       return false;
    }
    /**
     * 
     * @param login
     * @return vrai si le login est déjà inscrit dans la base.
     */
    @Override
    public boolean checkLogin(String login){
        String request = "SELECT COUNT(u) FROM User u WHERE u.login=:login";
        TypedQuery<Long> query = em.createQuery(request, Long.class);
        query.setParameter("login", login);
        Long count = query.getSingleResult();
        return count>0; //si le login existe déjà retourne true
    }
    
     //récupération de l'utilisateur ayant le login passé en paramètre
    @Override
    public User getRegisteredUser(String login){
        
        String request = "SELECT u FROM User u WHERE u.login=:login";
        TypedQuery<User> query = em.createQuery(request, User.class);
        query.setParameter("login", login);
        User u = query.getSingleResult();
        return u;//null est retourné s'il n'y a pas d'utilisateur pour le login passé
        
    }

    @Override
    /*
    liste tous les groupes utilisateurs disponibles en excluant le groupe administrateurs
    */
    public List<UserGroup> getAllAvailableCustomerGroup() {
        String request = "SELECT g FROM UserGroup g WHERE g.name <> 'administrateurs'";
        TypedQuery<UserGroup> query = em.createQuery(request, UserGroup.class);
        return query.getResultList();
    }
    
    
}
