package com.artqueen.logicuniversitystationerysystem.Representative.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.artqueen.logicuniversitystationerysystem.Employee.Data.Items;
import com.artqueen.logicuniversitystationerysystem.R;
import com.artqueen.logicuniversitystationerysystem.Representative.Activity.DisbursementDetailsActivity;
import com.artqueen.logicuniversitystationerysystem.Representative.Data.Disbursement;
import com.artqueen.logicuniversitystationerysystem.Representative.Data.DisbursementDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaikmdashiq on 3/3/15.
 */
public class DisbursementDetailsAdapter extends BaseAdapter implements ListAdapter {

    TextView itemNameTV,quantityTV;
    public static TextView pendingQty;
    public static String pendingQuantity;
    private List<DisbursementDetails> list = new ArrayList<DisbursementDetails>();
    private Context context;


    public DisbursementDetailsAdapter(List<DisbursementDetails> disbursementList, Context context)
    {
        this.list=disbursementList;
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
            view = inflater.inflate(R.layout.disbursement_details_row, null);
        }
        final View finalView = view;
        new AsyncTask<String, Void, Items>() {
            @Override
            protected Items doInBackground(String... params) {
                Items a;
                a=Items.getItem(params[0]);
                return a;
            }
            @Override
            protected void onPostExecute(Items result) {
                String itemName = result.get("description");
                itemNameTV = (TextView) finalView.findViewById(R.id.disbursementDetailsItemName);
                itemNameTV.setText("Item: "+itemName);
            }
        }.execute(list.get(position).get("itemId"));


        quantityTV = (TextView) view.findViewById(R.id.disbursementDetailsQuantity);
        quantityTV.setText("Quantity: "+(list.get(position).get("quantity")));

        pendingQty = (TextView) view.findViewById(R.id.disbursementDetailsPendingQty);
        pendingQuantity = (list.get(position).get("pendingAmount"));
        pendingQty.setText("Pending Quantity: "+pendingQuantity);
        notifyDataSetChanged();





        return view;
    }
}
