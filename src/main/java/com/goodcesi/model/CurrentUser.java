/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.domain.User;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author asbriglio
 * Wrapper d'User suivant le pattern VO.
 * Ce n'est pas un bean CDI car pas de scope définissant un bean CDI.
 * cette classe n'est pas considérée comme une classe/source de bean managé CDI
 * 
 */
@Dependent
public class CurrentUser implements Serializable { //implémentation de Serializable pour gérer le cas de la passivation
    
    private User user; //utilisateur authentifié empaqueté ("wrapped") dans le composant


     CurrentUser() { //nécessaire pour la création de l'instance "proxyable" par le container CDI   
    }
    
    
    public CurrentUser(User user) {
        this.user = user;
    }
   

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
   
    public void logout() throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        //invalidation de la session
        HttpSession currentSession = (HttpSession)ec.getSession(false);
        currentSession.invalidate();
        //on récupère une référence à la requête en cours car l'opération de redirection a lieu depuis différents emplacements
        //(depuis un dossier sécurisé, depuis la racine de l'appli). Il faut donc avoir une référence à l'adresse absolue de la page index. 
        HttpServletRequest request=   (HttpServletRequest) ec.getRequest();
        String baseUrl=request.getContextPath();
      //redirection - demande de la page index.xhtml
        ec.redirect(baseUrl+"/faces/index.xhtml?faces-redirect=true");
    }
    
}
