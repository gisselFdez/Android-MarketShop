package com.example.anagissel.marketshop.Factories;

import android.content.Context;
import com.example.anagissel.marketshop.DbHelper.DatabaseHelper;
import com.example.anagissel.marketshop.Entities.Element;
import com.example.anagissel.marketshop.Entities.ShoppingList;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ana Gissel on 10/17/2015.
 */
public class ElementFactory {

    DatabaseHelper dbHelper;
    RuntimeExceptionDao<Element,Integer> elementDao;

    /**
     * Constructor
     * @param context
     */
    public ElementFactory(Context context){
        dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    /**
     * Get all elements from shopping list
     * @param shoppingList
     * @return
     */
    public List<Element> getElementsFromList(ShoppingList shoppingList) {
        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();
        //query
        List<Element> lists;
        QueryBuilder<Element, Integer> queryBuilder = elementDao.queryBuilder();

        try {
            queryBuilder.where().eq("id_list",  shoppingList.getId_list());
            queryBuilder.orderBy("isActive", false);
            lists = queryBuilder.query();

            OpenHelperManager.releaseHelper();
            return lists;

        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
            return null;
        }
    }

    /**
     * create a shopping list item
     * @param description
     * @param shoppingList
     * @return
     */
    public List<Element> doCreateElementList(String description, float price,ShoppingList shoppingList){

        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();

        //create
        elementDao.create(new Element(shoppingList.getId_list(),description,true,price));
        //query
        List<Element> lists = elementDao.queryForAll();

        OpenHelperManager.releaseHelper();
        return lists;
    }

    /**
     * delete all items from a shopping list
     * @param shoppingList
     */
    public void doDelelteAllElements(ShoppingList shoppingList){
        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();

        DeleteBuilder<Element, Integer> deleteBuilder = elementDao.deleteBuilder();
        try {
            deleteBuilder.where().eq("id_list",shoppingList.getId_list());
            deleteBuilder.delete();
            OpenHelperManager.releaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * update an element description
     * @param element
     */
    public void doUpdateElement(Element element){
        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();

        UpdateBuilder<Element, Integer> updateBuilder = elementDao.updateBuilder();
        try {
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id_element", element.getId_element());
            // update the value of your field(s)
            updateBuilder.updateColumnValue("description", element.getDescription());
            updateBuilder.updateColumnValue("price", element.getPrice());
            updateBuilder.update();
            OpenHelperManager.releaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * delete an element
     * @param element
     */
    public void doDeleteElement(Element element){
        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();

        DeleteBuilder<Element, Integer> deleteBuilder = elementDao.deleteBuilder();
        try {
            deleteBuilder.where().eq("id_element", element.getId_element());
            deleteBuilder.delete();
            OpenHelperManager.releaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * update element status
     * @param element
     */
    public  void doUpdateElementStatus(Element element){
        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();

        UpdateBuilder<Element, Integer> updateBuilder = elementDao.updateBuilder();
        try {
            // set the criteria like you would a QueryBuilder
            updateBuilder.where().eq("id_element", element.getId_element());
            // update the value of your field(s)
            updateBuilder.updateColumnValue("isActive", !element.isActive());
            updateBuilder.update();
            OpenHelperManager.releaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * Calculate total from a shoppingList
     * @param shoppingList
     * @return
     */
    public float getTotal(ShoppingList shoppingList){
        elementDao = dbHelper.getElementIntegerRuntimeExceptionDao();

        GenericRawResults<String[]> rawResults = elementDao.queryRaw(
                "SELECT (CASE " +
                        "(Select Count(e.id_element) FROM element e WHERE e.id_list= "+shoppingList.getId_list()+
                        " AND e.isActive=1) "+
                        "WHEN 0 " +
                        "THEN 0 "+
                        "ELSE "+
                        "(select SUM(price) FROM element e WHERE e.id_list="+ shoppingList.getId_list()+
                        " AND e.isActive=1 ) "+
                        "END ) "+
                        "FROM element");
        try {
            List<String[]> results = rawResults.getResults();
            OpenHelperManager.releaseHelper();
            if(results.size() != 0) {
                String resultArray = Arrays.toString(results.get(0)).replace("[","").replace("]","").trim();
                return Float.parseFloat(resultArray);
            }
            else
                return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            OpenHelperManager.releaseHelper();
            return 0;
        }
    }
}
