package com.example.anagissel.marketshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Toast;
import com.example.anagissel.marketshop.Adapters.ShoppingListAdapter;
import com.example.anagissel.marketshop.Entities.ShoppingList;
import com.example.anagissel.marketshop.Factories.ShoppingListFactory;

public class MainActivity extends AppCompatActivity {

    ListView listView ;
    ArrayAdapter adaptator;
    ShoppingListFactory shoppingListFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Action Bar configuration
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"@color/white\">" + "MarketShop" + "</font>")));
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_logo);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.primary_dark)));
        setContentView(R.layout.main_activity);

        //ListView instance
        listView = (ListView)findViewById(R.id.list_view);
        updateAdaptor(listView);

        //List Name (EditText) instance
        final EditText edit = (EditText) findViewById(R.id.txtItem);

        //List Name onFocusChanged listener
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // hide keyboard
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        edit.clearFocus();
                    }
                }
            }
        });

        //On List Name click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShoppingList selectedList = (ShoppingList)adaptator.getItem(position);
                OpenListDetails(selectedList);
            }
        });

        //On Add List button click
        final Button button = (Button) findViewById(R.id.btnAdd);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String listName = edit.getText().toString();
                if(!listName.equals("")) {
                    shoppingListFactory = new ShoppingListFactory(getApplicationContext());
                    shoppingListFactory.doCreateShoppingList(listName);
                    updateAdaptor(listView);
                    edit.setText("");
                }
                else
                    showToast("You must enter a List name",Toast.LENGTH_SHORT);
            }
        });

        //On long click on List Item
        registerForContextMenu(listView);
    }

    //Create options menu to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //On Select an Option from de Action bar menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        shoppingListFactory = new ShoppingListFactory(getApplicationContext());
        switch (item.getItemId()) {
            case R.id.deleteAll:
                shoppingListFactory.doDelelteAllShoppingLists();
                updateAdaptor((ListView) findViewById(R.id.list_view));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Create context menu after a long click on a list item
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId()==R.id.list_view) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    //On Select an option from the context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ShoppingList shoppingList = (ShoppingList)((ListView)findViewById(R.id.list_view)).getItemAtPosition(menuInfo.position);
        shoppingListFactory = new ShoppingListFactory(getApplicationContext());

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                createEditDialog(shoppingList);
                return true;
            case R.id.delete:
                shoppingListFactory.doDeleteShoppingList(shoppingList);
                updateAdaptor((ListView) findViewById(R.id.list_view));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Create edit dialog for a Shopping list
     * @param shoppingList
     */
    private void createEditDialog(final ShoppingList shoppingList){
        shoppingListFactory = new ShoppingListFactory(getApplicationContext());
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Edit the list name");
        final EditText input = new EditText(this);
        input.setText(shoppingList.getTitle());
        builder1.setView(input);

        builder1.setCancelable(true);
        builder1.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(input.getText().toString().trim().equals("")){
                    showToast("You must enter a List name", Toast.LENGTH_SHORT);
                }
                else{
                    shoppingList.setTitle(input.getText().toString().trim());
                    shoppingListFactory.doUpdateShoppingList(shoppingList);
                    updateAdaptor((ListView) findViewById(R.id.list_view));
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
     * Go to list details activity
     * @param shoppingList
     */
    private void OpenListDetails(ShoppingList shoppingList){
        Intent listScreen = new Intent(getApplicationContext(), ListDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("shoppingList", shoppingList);
        listScreen.putExtras(bundle);

        startActivity(listScreen);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    /**
     * Update the list view adaptator
     * @param list
     */
    private void updateAdaptor(ListView list){
        shoppingListFactory = new ShoppingListFactory(getApplicationContext());
        adaptator = new ShoppingListAdapter<ShoppingList>(this, shoppingListFactory.doGetAllShoppingLists());
        listView.setAdapter(adaptator);
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
