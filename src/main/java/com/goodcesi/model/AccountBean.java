/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.model;

import com.goodcesi.business.domain.User;

import com.goodcesi.business.usermgmt.UserManagerLocal;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Asbriglio
 * bean chargé de la création d'un compte utilisateur
 */
@ApplicationScoped
@Named("accountModel")
public class AccountBean implements AccountModel{
 
    @Inject
    private UserManagerLocal userManager;
    
    private String login, pwd, fname, lname, email, address;
    private List<Long> selectedGroupIds;
   
    /*
        Par défaut un bean avec un scope de conversation est transient (vie le temps de la requête)
        conversation.begin() promeut l'instance en tant que bean de longue durée. l'instance sera associée 
        au scope de conversation jusqu'à conversation.end() invoquée. end() indique au container CDI que lorsque
        la méthode ayant invoquée end() se termine l'instance doit être détruite.
        */ 
    public String createCredential(){
       return "credInfos";
    }
       
    @Override
    public String createUser(){
       //vérification du login
       boolean existingLogin = userManager.checkLogin(login);
       if(existingLogin){//si le login existe déjà
           String msgSummary = "identifiant existant!";
           String msgDetail = "L'identifiant "+login+" est déjà pris. Choisissez-en un autre";
           FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,msgSummary, msgDetail));//on créé un message d'avertissement
           return "credInfos";//navigation vers la page de saisie des crédentiels.
       }
        
        //on devrait gérer le non renseignement des champs
        User u = new User(fname, lname);
        u.setEmail(email);
        u.setAddress(address);
        u.setLogin(login);
        //on encode le mot de passe
        pwd= encrypt(pwd);
        u.setPassword(pwd);
        
        userManager.create(u,selectedGroupIds);//ici le retour n'a pas d'importance
               
        //On clôture la session en cours
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession currentSession = (HttpSession)ec.getSession(false);
        currentSession.invalidate();
        
        return "index";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPwd() {
        return pwd;
    }

    @Override
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Long> getSelectedGroupIds() {
        return selectedGroupIds;
    }

    public void setSelectedGroupIds(List<Long> selectedGroupIds) {
        this.selectedGroupIds = selectedGroupIds;
    }
    
    private String encrypt(String password){
        try {
            //utilisation de la bibliothèque de sécurité Java SE  (Java Cryptography Architecture)
            
            MessageDigest digest = MessageDigest.getInstance("sha-256");//obtention d'un message digest pour le chiffrement en sha-256
            byte[] hash = digest.digest(password.getBytes("UTF-8"));//conversion du mot de passe en tableau d'octet en utilisant UTF-8 puis hachage
            StringBuilder encodedPwd = new StringBuilder();//mutable
            
            for(int i=0; i<hash.length;i++){//pour chaque octet
                //retourne la chaine représentant l'octet sous forme d'entier non signé en hexa( base 16)
                String hexValue = Integer.toHexString(0xff & hash[i]);
                if(hexValue.length() == 1) encodedPwd.append('0');
                encodedPwd.append(hexValue);  
            }
            
            return encodedPwd.toString();
                
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new RuntimeException("impossible de chiffrer le mot de passe");
        }
    }
}
