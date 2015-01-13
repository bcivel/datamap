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
    public Datamap create(Integer id,String stream,String application,String page,String locationType, String locationValue,String implemented, String zone, String picture, String comment) {
        Datamap datamap = new Datamap();
        datamap.setLocationType(locationType);
        datamap.setLocationValue(locationValue);
        datamap.setId(id);
        datamap.setImplemented(implemented);
        datamap.setPage(page);
        datamap.setStream(stream);
        datamap.setZone(zone);
        datamap.setPicture(picture);
        datamap.setComment(comment);
        datamap.setApplication(application);
        return datamap;
    }

}
