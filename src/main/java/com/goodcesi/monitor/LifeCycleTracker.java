/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.monitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author Asbriglio
 * un PhaseListener est un singleton accéder par toute les requêtes
 * déclaré dans faces-config
 */
public class LifeCycleTracker implements PhaseListener {
    
    private AtomicInteger count = new AtomicInteger(1);
    
    @Override
    public void beforePhase(PhaseEvent event) {
        System.out.println("------PHASE = "+event.getPhaseId());
      }
    

    @Override
    public synchronized void afterPhase(PhaseEvent event) {
        //si la phase est render response
        if(event.getPhaseId()==PhaseId.RENDER_RESPONSE){
            Integer requestNumber = count.getAndIncrement();//on récupère la val du compteur et on incrémente celui-ci
            System.out.println("------Fin cycle de vie de la requête n° "+requestNumber);
        }  
    }
    
    /*
    spécifie pour quelle étape/phase du cycle de vie le Listener doit éxécuter after/beforePhase
    */
    @Override
    public PhaseId getPhaseId() {
      return PhaseId.ANY_PHASE; 
    }

   
}
