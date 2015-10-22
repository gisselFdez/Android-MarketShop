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
 * Created by Ana Gissel on 10/5/2015.
 */
public class ElementAdapter<T> extends ArrayAdapter<T> {

    /**
     * Constructor
     * @param context
     * @param objects
     */
    public ElementAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
    }

    /**
     * Method that returns the view for an element's ListView
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
                    R.layout.list_element_item_layout,
                    parent,
                    false);
        }

        //Getting instances from text views
        TextView description = (TextView)listItemView.findViewById(R.id.text1);
        ImageView status = (ImageView)listItemView.findViewById(R.id.status);
        TextView price = (TextView)listItemView.findViewById(R.id.text2);

        T item = (T)getItem(position);

        //Get element information
        String holeText;
        String subText [];
        String splitter = "\\|";

        holeText = item.toString();
        subText = holeText.split(splitter, holeText.length());

        //get description
        description.setText(subText[2]);

        //get if the element is active
        int image = 0;
        if(Boolean.parseBoolean(subText[3]))
            image = R.drawable.abc_btn_check_to_on_mtrl_000;
        else
            image = R.drawable.abc_btn_check_to_on_mtrl_015;
        status.setImageResource(image);

        //get price
        if(Float.parseFloat(subText[4]) == 0)
            price.setText("");
        else
            price.setText("$ "+subText[4]);

        return listItemView;
    }
}
