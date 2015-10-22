package com.example.anagissel.marketshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.anagissel.marketshop.Adapters.ElementAdapter;
import com.example.anagissel.marketshop.Entities.Element;
import com.example.anagissel.marketshop.Entities.ShoppingList;
import com.example.anagissel.marketshop.Factories.ElementFactory;

/**
 * Created by Ana Gissel on 9/19/2015.
 */
public class ListDetailsActivity extends AppCompatActivity {

    ListView listView ;
    ShoppingList shoppingList;
    ArrayAdapter adaptator;
    ElementFactory elementFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the intent
        shoppingList = (ShoppingList) getIntent().getExtras().getSerializable("shoppingList");

        //ActionBar configuration
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary_dark)));
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"@color/white\">" + shoppingList.getTitle() + "</font>")));
        getSupportActionBar().setIcon(R.mipmap.ic_list_logo);
        setContentView(R.layout.list_details_activity);

        //ListView instance
        listView = (ListView)findViewById(R.id.details_list_view);
        updateAdaptor(listView, shoppingList);

        //List Name (EditText) instance
        final EditText edit = (EditText) findViewById(R.id.txtItemList);

        //Item Name onFocusChanged listener
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // hide keyboard
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        edit.clearFocus();
                    }
                }
            }
        });

        //Item price (EditText) instance
        final EditText editPrice = (EditText) findViewById(R.id.txtItemPrice);
        //Item price onFocusChanged listener
        editPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // hide keyboard
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        editPrice.clearFocus();
                    }
                }
            }
        });

        //On List item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Element selectedList = (Element) adaptator.getItem(position);
                doChangeElementStatus(selectedList);
                updateAdaptor(listView, shoppingList);
            }
        });

        //On click on add item button
        final Button button = (Button) findViewById(R.id.btnAddElement);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                EditText edit = (EditText) findViewById(R.id.txtItemList);
                EditText editPrice = (EditText) findViewById(R.id.txtItemPrice);
                String itemDescription = edit.getText().toString();
                String priceString = editPrice.getText().toString();
                if (!itemDescription.equals("")) {
                    float price =0;
                    if(!priceString.equals(""))
                        price = Float.parseFloat(priceString);
                    elementFactory = new ElementFactory(getApplicationContext());
                    elementFactory.doCreateElementList(itemDescription, price, shoppingList);
                    updateAdaptor(listView, shoppingList);
                    edit.setText("");
                    editPrice.setText("");
                } else
                    showToast("You must enter the Item name", Toast.LENGTH_SHORT);
            }
        });

        //On long click on List Item
        listView = (ListView) findViewById(R.id.details_list_view);
        registerForContextMenu(listView);
    }

    //Create options menu to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //On Select an Option from de Action bar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        elementFactory = new ElementFactory(getApplicationContext());
        switch (item.getItemId()) {
            case R.id.deleteAll:
                elementFactory.doDelelteAllElements(shoppingList);
                updateAdaptor((ListView) findViewById(R.id.list_view),shoppingList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Create context menu after a long click on a list item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId()==R.id.details_list_view) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    //On Select an option from the context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Element element = (Element)((ListView)findViewById(R.id.details_list_view)).getItemAtPosition(menuInfo.position);
        elementFactory = new ElementFactory(getApplicationContext());

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                createEditDialog(element);
                return true;
            case R.id.delete:
                elementFactory.doDeleteElement(element);
                updateAdaptor((ListView) findViewById(R.id.list_view), shoppingList);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainScreen);
        finish();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }

    /**
     * Create Edit dialog for an element
     * @param element
     */
    private void createEditDialog(final Element element){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Edit the item");
        elementFactory = new ElementFactory(getApplicationContext());

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_item_dialog_layout, null);

        final EditText description = (EditText)view.findViewById(R.id.description);
        description.setText(element.getDescription());

        final EditText price =(EditText) view.findViewById(R.id.price);
        if(element.getPrice() != 0)
            price.setText(Float.toString(element.getPrice()));
        price.setHint(R.string.price);

        builder1.setView(view);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(description.getText().toString().trim().equals(""))
                    showToast("You must enter the Item name", Toast.LENGTH_SHORT);
                else
                {
                    element.setDescription(description.getText().toString().trim());
                    float newPrice =0;
                    if(!(price.getText().toString()).equals(""))
                        newPrice = Float.parseFloat((price.getText().toString()));
                    element.setPrice(newPrice);
                    elementFactory.doUpdateElement(element);
                    updateAdaptor((ListView) findViewById(R.id.details_list_view),shoppingList);
                }
            }
        });
        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * Changes the item status
     * @param element
     */
    private void doChangeElementStatus(Element element){
        elementFactory = new ElementFactory(getApplicationContext());
        elementFactory.doUpdateElementStatus(element);
    }

    /**
     * Update the total of a shopping list
     * @param shoppingList
     */
    private void doUpdateTotal(ShoppingList shoppingList){
        elementFactory = new ElementFactory(getApplicationContext());
        TextView total = (TextView)this.findViewById(R.id.total);
        float result = elementFactory.getTotal(shoppingList);
        if(result>0)
            total.setText("$ "+Float.toString(result));
        else
            total.setText("");
    }

    /**
     * Update the list view adaptator
     * @param list
     * @param shoppingList
     */
    private void updateAdaptor(ListView list,ShoppingList shoppingList){
        elementFactory = new ElementFactory(getApplicationContext());
        adaptator = new ElementAdapter(this, elementFactory.getElementsFromList(shoppingList));
        listView.setAdapter(adaptator);
        doUpdateTotal(shoppingList);
    }

    /**
     * show toast
     * @param text
     * @param duration
     */
    private void showToast(String text,int duration){
        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

}
