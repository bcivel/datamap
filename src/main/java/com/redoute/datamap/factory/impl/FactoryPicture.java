/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redoute.datamap.factory.impl;

import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.factory.IFactoryDatamap;
import com.redoute.datamap.factory.IFactoryPicture;
import org.springframework.stereotype.Service;

/**
 * @author bcivel
 */
@Service
public class FactoryPicture implements IFactoryPicture {

    @Override
    public Picture create(Integer id,String application,String page,String picture,String base64) {
        Picture pict = new Picture();
        pict.setId(id);
        pict.setPicture(picture);
        pict.setPage(page);
        pict.setBase64(base64);
        pict.setApplication(application);
        return pict;
    }

}
