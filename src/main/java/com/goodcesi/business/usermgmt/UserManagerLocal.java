/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.usermgmt;

import com.goodcesi.business.domain.User;
import com.goodcesi.business.domain.UserGroup;
import java.util.List;

/**
 *
 * @author Asbriglio
 */
public interface UserManagerLocal {
    
    public boolean create(User u, List<Long> groupIds);
    
    public boolean checkLogin(String login);

    public User getRegisteredUser(String login);
    
    /**
     * 
     * @return la liste des groupes « clients » disponibles
     */
    public List<UserGroup> getAllAvailableCustomerGroup();
    
}
