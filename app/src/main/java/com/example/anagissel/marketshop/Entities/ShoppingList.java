package com.example.anagissel.marketshop.Entities;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by Ana Gissel on 9/26/2015.
 */
public class ShoppingList implements Serializable{
    @DatabaseField(generatedId = true)
    private int id_list;

    @DatabaseField
    private String title;

    public ShoppingList(String title){
        //this.id_list = id_list;
        super();
        this.title = title;
    }

    public ShoppingList(){
    }

    public void setId_list(int id_list){
        this.id_list = id_list;
    }
    public void setTitle(String title){
        this.title = title;}

    public int getId_list(){return id_list;}
    public String getTitle(){return title;}

    @Override
    public String toString(){return id_list+"|"+title;}
}
