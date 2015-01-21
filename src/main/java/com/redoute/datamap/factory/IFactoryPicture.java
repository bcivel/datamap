/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redoute.datamap.factory;

import com.redoute.datamap.entity.Picture;


/**
 *
 * @author bcivel
 */
public interface IFactoryPicture {
    
    Picture create(Integer id,String application, String page,String picture,String base64);
    
    Picture create(Integer id, String application, String page, String picture, String base64, String localPath);
    
}
