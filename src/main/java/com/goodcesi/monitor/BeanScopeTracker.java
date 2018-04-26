/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.monitor;

import com.goodcesi.qualifier.ScopeMonitor;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;


/**
 *
 * @author Asbriglio
 * intercepteur de cycle de vie des beans CDI.
 * On est pas obligé d'invoquer ctx.proceed() car il n'y a pas d'autre callback de cycle de vie dans la chaine d'interception.
 */
@Interceptor //déclaration de l'interceptor
@ScopeMonitor //déclaration de la liaison pour l'interception des cycles de vie
@Priority(Interceptor.Priority.APPLICATION +1 )//active l'interceptor
public class BeanScopeTracker implements Serializable{//Serializable pour pouvoir être associés à des beans de scope pouvant entrainer la passivation.
    
    @PostConstruct
    void init(InvocationContext ctx){
       String targetName = ctx.getTarget().getClass().getName();
       System.out.println("****instance de "+targetName+ " construite****");
        try {
            ctx.proceed();//appel du prochain callback si présent
        } catch (Exception ex) {
            Logger.getLogger(BeanScopeTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PreDestroy
    void destroy(InvocationContext ctx){
        String targetName = ctx.getTarget().getClass().getName();
        System.out.println("****instance de "+targetName+ " va être détruite****");
        try {
            ctx.proceed();//appel du prochain callback si présent
        } catch (Exception ex) {
            Logger.getLogger(BeanScopeTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
}
