/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redoute.datamap.factory;

import com.redoute.datamap.entity.Datamap;


/**
 *
 * @author bcivel
 */
public interface IFactoryDatamap {
    
    Datamap create(Integer id,String stream,String page,String datacerberus,String implemented, String xpath, String picture, String comment);
}
