package com.example.anagissel.marketshop.Entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Ana Gissel on 10/5/2015.
 */
public class Element {
    @DatabaseField(generatedId = true)
    private int id_element;

    @DatabaseField
    private int id_list;

    @DatabaseField
    private String description;

    @DatabaseField
    private boolean isActive;

    @DatabaseField
    private float price;

    public Element() {
    }

    public Element(int id_list, String description, boolean isActive, float price) {
        this.id_list = id_list;
        this.description = description;
        this.isActive = isActive;
        this.price = price;
    }

    public int getId_element() {
        return id_element;
    }

    public void setId_element(int id_element) {
        this.id_element = id_element;
    }

    public int getId_list() {
        return id_list;
    }

    public void setId_list(int id_list) {
        this.id_list = id_list;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString(){return id_element+"|"+id_list+"|"+description+"|"+isActive+"|"+price;}
}
