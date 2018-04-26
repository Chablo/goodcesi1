/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.goodcesi.integration.dao;

import java.util.List;

/**
 *
 * @author cesi
 */
public interface CrudServiceLocal {
    public <T> T create (T t);
    public <T> T find(Class<T> type, Long id);//on retourne l'entité
    public <T> T update(T t);
    public void delete(Object t);
    public <T> List<T> findAll(Class<T> type);
    
}
