/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redoute.datamap.factory.impl;

import org.springframework.stereotype.Service;

import com.redoute.datamap.entity.Picture;
import com.redoute.datamap.factory.IFactoryPicture;

/**
 * @author bcivel
 */
@Service
public class FactoryPicture implements IFactoryPicture {

    @Override
    public Picture create(Integer id,String application,String page,String picture,String base64) {
       return create(id, application, page, picture, base64, "");
    }

	@Override
	public Picture create(Integer id, String application, String page, String picture, String base64, String localPath) {
		 Picture pict = new Picture();
		pict.setId(id);
		pict.setPicture(picture);
		pict.setPage(page);
		pict.setBase64(base64);
		pict.setApplication(application);
		pict.setLocalPath(localPath);
		return pict;
	}

}
