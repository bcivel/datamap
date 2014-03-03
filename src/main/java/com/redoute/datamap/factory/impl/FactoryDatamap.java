/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redoute.datamap.factory.impl;

import com.redoute.datamap.entity.Datamap;
import com.redoute.datamap.factory.IFactoryDatamap;
import org.springframework.stereotype.Service;

/**
 * @author bcivel
 */
@Service
public class FactoryDatamap implements IFactoryDatamap {

    @Override
    public Datamap create(Integer id,String stream,String page,String datacerberus,String implemented, String xpath, String picture) {
        Datamap datamap = new Datamap();
        datamap.setDatacerberus(datacerberus);
        datamap.setId(id);
        datamap.setImplemented(implemented);
        datamap.setPage(page);
        datamap.setStream(stream);
        datamap.setXpath(xpath);
        datamap.setPicture(picture);
        return datamap;
    }

}
