/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.qualifier;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.interceptor.InterceptorBinding;

/**
 *
 * @author Asbriglio
 * Annotation pour lier un intercepteur de cycle de vie à un composant managé : binding type
 */

@InterceptorBinding
@Retention(RUNTIME)
@Target({TYPE})//pour les annoatations de liaison d'intercepteurs de cycle de vie seul TYPE est autorisé
public @interface ScopeMonitor {
}
