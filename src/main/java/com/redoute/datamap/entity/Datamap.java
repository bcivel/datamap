/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redoute.datamap.entity;

/**
 *
 * @author bcivel
 */
public class Datamap {

	public static final String LOCATION_SEPARATOR = "=";
	
    private Integer id;
    private String stream;
    private String application;
    private String page;
    private String locationType;
    private String locationValue;
    private String implemented;
    private String picture;
    private String zone;
    private String comment;

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getLocationType() {
    	return locationType;
    }
    
    public void setLocationType(String locationType) {
    	this.locationType = locationType;
    }
    
    public String getLocationValue() {
    	return locationValue;
    }
    
    public void setLocationValue(String locationValue) {
    	this.locationValue = locationValue;
    }
    
    public String getLocation() {
    	return getLocationType() + LOCATION_SEPARATOR + getLocationValue();
    }
    
    public String getImplemented() {
        return implemented;
    }

    public void setImplemented(String implemented) {
        this.implemented = implemented;
    }

}
