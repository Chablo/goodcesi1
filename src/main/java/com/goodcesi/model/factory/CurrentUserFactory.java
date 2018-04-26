/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model.factory;

import com.goodcesi.business.domain.User;
import com.goodcesi.business.usermgmt.UserManagerLocal;
import com.goodcesi.model.CurrentUser;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;


/**
 *
 * @author asbriglio
 * Fabrique produisant une représentation de l'utilisateur authentifié avant injection de cette
 * représentation aux points d'injection
 */
@Dependent //l'injection n'étant possible que dans un composant managé, on déclare la fabrique en tant que bean CDI à pseudo-scope. 
public class CurrentUserFactory implements Serializable{
    
    @Inject
    private UserManagerLocal userManager;
    
    /*
    produit une instance "utilisateur authentifié" liée à la session en cours. Cette instance est cachée derrière un proxy client. 
    chaque invocation passe par le proxy. l'instance est créée lors de la première invocation au sein de la session : instanciation différée
    le proxy client est injecté aux points d'injection.
    
    !!! La méthode productrice ne doit pas être invoquée avant que l'authentification JAAS ait eu lieu SINON échec
    l'authentification a lieu lorsque l'on accède à une page protégé.
    */
  
    CurrentUser createCurrentUser(){
        System.out.println("-----------------------------méthode @Produces déclénchée");
        String userLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        System.out.println("-----------------------------utilisateur loggé "+userLogin);
        
        User u = userManager.getRegisteredUser(userLogin);
        if(u==null){//si le login ne correspond pas à un compte enregistré
            //cette exception n'est levée que dans le cas ou un développeur associerait CurrentUser 
            //à un espace non protégé par authentification.
            throw new RuntimeException("utilisateur non authentifié");//on aurait pu créer une exception personnalisée
        }
        CurrentUser currentUser = new CurrentUser(u); 
        return currentUser;       
        
    }
    
}
