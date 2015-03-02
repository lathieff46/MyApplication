package com.artqueen.logicuniversitystationerysystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shaikmdashiq on 2/3/15.
 */
public class UpdateCartAdapter extends BaseAdapter implements ListAdapter {
    private List<Items> list = new ArrayList<Items>();
    private Context context;
    public int RequisitionID;
    public UpdateCartAdapter(List<Items> itemsList, Context context)
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

        final TextView listItemText = (TextView)view.findViewById(R.id.text1);
        String des=((HashMap<String,String>)list.get(position)).get("description");
        listItemText.setText(des);

        TextView listItemText1 = (TextView)view.findViewById(R.id.text2);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are confirm delete the Requisition for this Item!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Items a = list.get(position);
                                Log.e(">>",""+a.get("itemId"));
                                RequisitionDetails.deleteRequisitionDetail(a,RequisitionID);
                                list.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .show();
            }
        });
        return view;
    }

}
