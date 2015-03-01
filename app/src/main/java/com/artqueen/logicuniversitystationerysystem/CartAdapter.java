package com.artqueen.logicuniversitystationerysystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shaikmdashiq on 28/2/15.
 */
public class CartAdapter extends BaseAdapter implements ListAdapter {
    private List<Items> list = new ArrayList<Items>();
    private Context context;
    public CartAdapter(List<Items> itemsList, Context context)
    {
        this.list=itemsList;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cart_row, null);
        }
        TextView listItemText = (TextView)view.findViewById(R.id.text1);
        String des=((HashMap<String,String>)list.get(position)).get("description");
        listItemText.setText(des);

        TextView listItemText1 = (TextView)view.findViewById(R.id.text2);
        String quantity=((HashMap<String,String>)list.get(position)).get("qty");
        listItemText1.setText("Quantity: "+quantity);


        TextView listItemText2 = (TextView) view.findViewById(R.id.text3);
        String uom =((HashMap<String,String>)list.get(position)).get("unitOfMeasure");
        listItemText2.setText("Unit of Measure: "+uom);

        Button remove = (Button) view.findViewById(R.id.removeBtn);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
