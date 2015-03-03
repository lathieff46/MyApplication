package com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.artqueen.logicuniversitystationerysystem.Employee.Activities.MakeRequest;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Items;
import com.artqueen.logicuniversitystationerysystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shaikmdashiq on 28/2/15.
 */
public class CartAdapter extends BaseAdapter implements ListAdapter {
    public static TextView listItemText1;
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

        listItemText1 = (TextView)view.findViewById(R.id.text2);
        String quantity=((HashMap<String,String>)list.get(position)).get("qty");
        listItemText1.setText("Quantity: "+quantity);
        notifyDataSetChanged();


        TextView listItemText2 = (TextView) view.findViewById(R.id.text3);
        String uom =((HashMap<String,String>)list.get(position)).get("unitOfMeasure");
        listItemText2.setText("Unit of Measure: "+uom);

        Button remove = (Button) view.findViewById(R.id.removeBtn);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String des=((HashMap<String,String>)list.get(position)).get("description");
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Remove "+des+" from the Cart ?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                 list.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context,des+" removed from Cart",Toast.LENGTH_SHORT).show();
                                if(list.isEmpty()){
                                    context.startActivity(new Intent(context, MakeRequest.class));
                                }
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });
        return view;
    }
}
