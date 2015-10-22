package com.example.anagissel.marketshop.Factories;

import android.content.Context;
import com.example.anagissel.marketshop.DbHelper.DatabaseHelper;
import com.example.anagissel.marketshop.Entities.Element;
import com.example.anagissel.marketshop.Entities.ShoppingList;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ana Gissel on 10/17/2015.
 */
public class ShoppingListFactory {

    DatabaseHelper dbHelper;
    RuntimeExceptionDao<ShoppingList,Integer> shoppingDao;
    RuntimeExceptionDao<Element,Integer> elementDao;

    /**
     * Constructor
     * @param context
     */
    public ShoppingListFactory(Context context)
    {
        dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    /**
     * Get all shopping lists from DB
     * @return
     */
    public List<ShoppingList> doGetAllShoppingLists() {
        shoppingDao = dbHelper.getShoppingListIntegerRuntimeExceptionDao();

        //query
        List<ShoppingList> lists = shoppingDao.queryForAll();

        OpenHelperManager.releaseHelper();
        return lists;
    }

    /**
     * create a shopping list
     * @param title
     * @return
     */
    public List<ShoppingList> doCreateShoppingList(String title){
        shoppingDao = dbHelper.getShoppingListIntegerRuntimeExceptionDao();

        //create
        shoppingDao.create(new ShoppingList(title));
        //query
        List<ShoppingList> lists = shoppingDao.queryForAll();
        OpenHelperManager.releaseHelper();
        return lists;
    }

    /**
     * delete all shopping lists from de DB
     */
    public void doDelelteAllShoppingLists(){
        shoppingDao = dbHelper.getShoppingListIntegerRuntimeExceptionDao();
        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();

        DeleteBuilder<Element, Integer> deleteBuilderDetails = elementDao.deleteBuilder();
        DeleteBuilder<ShoppingList, Integer> deleteBuilder = shoppingDao.deleteBuilder();
        try {
            //Shopping list items
            deleteBuilderDetails.where().isNotNull("description");
            deleteBuilderDetails.delete();

            //Shoppinglist
            deleteBuilder.where().isNotNull("title");
            deleteBuilder.delete();
            OpenHelperManager.releaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * update a shoppinglist's title
     * @param shoppingList
     */
    public void doUpdateShoppingList(ShoppingList shoppingList){
        shoppingDao = dbHelper.getShoppingListIntegerRuntimeExceptionDao();

        UpdateBuilder<ShoppingList, Integer> updateBuilder = shoppingDao.updateBuilder();
        try {
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id_list", shoppingList.getId_list());
            // update the value of your field(s)
            updateBuilder.updateColumnValue("title", shoppingList.getTitle());
            updateBuilder.update();
            OpenHelperManager.releaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * delete a shopping list
     * @param shoppingList
     */
    public void doDeleteShoppingList(ShoppingList shoppingList){
        shoppingDao = dbHelper.getShoppingListIntegerRuntimeExceptionDao();
        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();

        DeleteBuilder<Element, Integer> deleteBuilderDetails = elementDao.deleteBuilder();
        DeleteBuilder<ShoppingList, Integer> deleteBuilder = shoppingDao.deleteBuilder();
        try {
            //Shopping list items
            deleteBuilderDetails.where().eq("id_list",shoppingList.getId_list());
            deleteBuilderDetails.delete();

            //Shoppinglist
            deleteBuilder.where().eq("id_list",shoppingList.getId_list());
            deleteBuilder.delete();
            OpenHelperManager.releaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
        }
    }
}
