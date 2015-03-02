package com.artqueen.logicuniversitystationerysystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by shaikmdashiq on 1/3/15.
 */
public class UpdateRequisitionAdapter extends BaseAdapter implements ListAdapter {
    TextView dateTv,requisitionNum;
    //Button update,delete;
    private List<Requisition> list = new ArrayList<Requisition>();
    private Context context;


    static List<Items> cart=new ArrayList<Items>();

    public UpdateRequisitionAdapter(List<Requisition> requisitionList, Context context)
    {
        this.list=requisitionList;
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
            view = inflater.inflate(R.layout.update_requisition_row, null);
        }
        dateTv = (TextView)view.findViewById(R.id.dateTV);
        dateTv.setText("Date: "+list.get(position).get("Date"));

        requisitionNum = (TextView) view.findViewById(R.id.requitionNumTV);
        requisitionNum.setText("Requisition id: "+(list.get(position).get("requisitionID")));

        Button update = (Button) view.findViewById(R.id.updaterequisitionBtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<String, Void, List<RequisitionDetails>>() {
                    @Override
                    protected List<RequisitionDetails> doInBackground(String... params) {
                        return  RequisitionDetails.list(params[0]);
                    }
                    @Override
                    protected void onPostExecute(List<RequisitionDetails> result) {
                        for(RequisitionDetails c:result)
                        {
                            Log.e(">>","-------- Itemid:"+c.get("itemId"));
                            Items one=Items.getItem(c.get("itemId"));
                            Log.e(">>",""+one.get("description"));
                            one.saveQty(c.get("qty"));
                            Log.e(">>",""+one.get("qty"));
                            cart.add(one);
                        }
                        for(int i =0;i<cart.size();i++)
                        {
                            Log.e("From cart>>",""+cart.get(i).get("description"));
                        }
                    }
                }.execute(list.get(position).get("requisitionID"));

                Intent i=new Intent(context,UpdateCart.class);
                i.putExtra("updateCart", (java.io.Serializable) cart);
                UpdateCart.RequisitionID=Integer.valueOf(list.get(position).get("requisitionID"));
                context.startActivity(i);


            }
        });

        Button delete = (Button) view.findViewById(R.id.deleteRequisitionBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete this Requisition?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                int rid=Integer.valueOf(list.get(position).get("requisitionID"));
                                Requisition.DeleteRequistion(rid);
                                Log.e(">>>>>Deleteid:",""+rid);
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