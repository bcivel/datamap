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

    private Integer id;
    private String stream;
    private String page;
    private String datacerberus;
    private String implemented;
    private String picture;
    private String xpath;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
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

    public String getDatacerberus() {
        return datacerberus;
    }

    public void setDatacerberus(String datacerberus) {
        this.datacerberus = datacerberus;
    }

    public String getImplemented() {
        return implemented;
    }

    public void setImplemented(String implemented) {
        this.implemented = implemented;
    }

}
