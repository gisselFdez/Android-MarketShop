package com.example.anagissel.marketshop.DbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.anagissel.marketshop.Entities.Element;
import com.example.anagissel.marketshop.Entities.ShoppingList;
import com.example.anagissel.marketshop.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Ana Gissel on 10/5/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "shoppingList.db";
    private static final int DATABASE_VERSION =2;
    private Dao<ShoppingList,Integer> shoppingListIntegerDao = null;
    private RuntimeExceptionDao<ShoppingList,Integer> shoppingListIntegerRuntimeExceptionDao = null;
    private Dao<Element,Integer> elementIntegerDao = null;
    private RuntimeExceptionDao<Element,Integer> elementIntegerRuntimeExceptionDao = null;

    /**
     * Constructor
     * @param context
     */
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * OnCrate Database
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ShoppingList.class);
            TableUtils.createTable(connectionSource, Element.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * On Update Database
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Element.class, true);
            TableUtils.dropTable(connectionSource,ShoppingList.class,true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Dao<ShoppingList,Integer> getShoppingListDao() throws SQLException {
        if (shoppingListIntegerDao == null){
            shoppingListIntegerDao = getDao(ShoppingList.class);
        }
        return shoppingListIntegerDao;
    }

    /**
     *
     * @return
     */
    public RuntimeExceptionDao<ShoppingList,Integer> getShoppingListIntegerRuntimeExceptionDao(){
        if (shoppingListIntegerDao ==null){
            shoppingListIntegerRuntimeExceptionDao = getRuntimeExceptionDao(ShoppingList.class);
        }
        return shoppingListIntegerRuntimeExceptionDao;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public Dao<Element,Integer> getElementIntegerDao() throws SQLException{
        if (elementIntegerDao == null){
            elementIntegerDao = getDao(Element.class);
        }
        return elementIntegerDao;
    }

    /**
     *
     * @return
     */
    public RuntimeExceptionDao<Element,Integer> getElementIntegerRuntimeExceptionDao(){
        if (elementIntegerDao ==null){
            elementIntegerRuntimeExceptionDao = getRuntimeExceptionDao(Element.class);
        }
        return elementIntegerRuntimeExceptionDao;
    }
}
