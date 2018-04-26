/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goodcesi.business.catalogmgmt;

import com.goodcesi.business.domain.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author asbriglio
 */

public interface CatalogManagerLocal {

    public Category saveNewCategory(String title);
    public Item saveNewItem(Item item,User owner, Set<Long> categories);//on passe en argument un Item plutôt que les propriétés de l'Item car elle sont nombreuses
    public List<Item> getItemsByCatId(Long catId);//peut être utiliser une liste de DTO comme type de retour
    
    public List<Category> getAllCategories();
    
    public Item getItemById(Long id);

    public Map<String, List<Item>> getItemsFromSeller(Long sellerId);

    public void updateItems(List<Item> items);
    
}
