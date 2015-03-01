package com.artqueen.logicuniversitystationerysystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter implements ListAdapter {
    private List<Items> list = new ArrayList<Items>();
    private Context context;


    public MyAdapter(List<Items> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.make_request_row, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.text1);
        listItemText.setText(list.get(position).get("description"));

        TextView listItemText2 = (TextView)view.findViewById(R.id.text2);
        listItemText2.setText(list.get(position).get("category"));


        final Button addBtn = (Button)view.findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               Items a = (Items) list.get(position);
                if(!MakeRequest.cart.contains(a)) {
                    MakeRequest.cart.add(a);
                    list.remove(a);
                }else
                    Toast.makeText(context,"Item Already in Cart",Toast.LENGTH_SHORT).show();

                notifyDataSetChanged();
            }
        });

        return view;
    }
}