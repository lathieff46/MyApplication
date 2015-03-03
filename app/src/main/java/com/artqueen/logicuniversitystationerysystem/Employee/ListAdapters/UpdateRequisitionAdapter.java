package com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.artqueen.logicuniversitystationerysystem.HomePage;
import com.artqueen.logicuniversitystationerysystem.Employee.Activities.UpdateCart;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Items;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Requisition;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.RequisitionDetails;
import com.artqueen.logicuniversitystationerysystem.R;

import java.util.ArrayList;

import java.util.List;

/**
 * Created by shaikmdashiq on 1/3/15.
 */
public class UpdateRequisitionAdapter extends BaseAdapter implements ListAdapter {
    TextView dateTv,requisitionNum;
    //Button update,delete;
    private List<Requisition> list = new ArrayList<Requisition>();
    private Context context;


   public static List<Items> cart=new ArrayList<Items>();

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
        requisitionNum.setText("Requisition Id: "+(list.get(position).get("requisitionID")));

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

                            Items one=Items.getItem(c.get("itemId"));

                            one.saveQty(c.get("qty"));

                            cart.add(one);
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
                                int rid = Integer.valueOf(list.get(position).get("requisitionID"));
                                Requisition.DeleteRequisition(rid);
                                list.remove(position);
                                notifyDataSetChanged();
                                if(list.isEmpty()){
                                    context.startActivity(new Intent(context, HomePage.class));
                                }
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