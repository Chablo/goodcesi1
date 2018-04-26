/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.catalogmgmt;

import com.goodcesi.business.domain.Category;
import com.goodcesi.business.domain.Item;
import com.goodcesi.business.domain.ItemStatus;
import com.goodcesi.business.domain.User;
import java.util.List;

/**
 *
 * @author asbriglio
 */

public interface ItemsSeekerLocal {

    public List<Item> retrieveUnsoldItemsByCategory(Category category);

    public List<Item> retrieveItemsFromUserByStatus(User user, ItemStatus status);
    
}
