package com.example.anagissel.marketshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.anagissel.marketshop.R;

import java.util.List;

/**
 * Created by Ana Gissel on 9/26/2015.
 */
//ShoppingListAdapter
public class ShoppingListAdapter<T> extends ArrayAdapter<T> {

    /**
     * Constructor
     * @param context
     * @param objects
     */
    public ShoppingListAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
    }

    /**
     * Method that returns the view for a shopping list's ListView
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //inflater instance
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Saving reference from View from row
        View listItemView = convertView;

        if (null == convertView) {
            listItemView = inflater.inflate(
                    R.layout.list_item_layout,
                    parent,
                    false);
        }

        //Getting instances from text views
        ImageView list = (ImageView)listItemView.findViewById(R.id.listImg);
        TextView title = (TextView)listItemView.findViewById(R.id.text1);
        ImageView next = (ImageView)listItemView.findViewById(R.id.nextImg);

        T item = (T)getItem(position);

        String holeText;
        String subText [];
        String splitter = "\\|";

        holeText = item.toString();
        subText = holeText.split(splitter, holeText.length());

        title.setText(subText[1]);

        list.setImageResource(R.mipmap.ic_list);
        next.setImageResource(R.mipmap.ic_next);
        return listItemView;
    }
}
