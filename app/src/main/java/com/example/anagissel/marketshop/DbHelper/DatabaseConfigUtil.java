package com.example.anagissel.marketshop.DbHelper;

import com.example.anagissel.marketshop.Entities.Element;
import com.example.anagissel.marketshop.Entities.ShoppingList;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Ana Gissel on 10/5/2015.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil{

    public static final Class<?>[] classes = new Class[]{
            ShoppingList.class,
            Element.class};

    public static  void main(String[] args) throws IOException, SQLException {
        writeConfigFile(new File("C:\\Users\\AnaGissel\\AndroidStudioProjects\\MarketShop2\\app\\src\\main\\res\\raw\\ormlite_config.txt"),classes);

    }
}
