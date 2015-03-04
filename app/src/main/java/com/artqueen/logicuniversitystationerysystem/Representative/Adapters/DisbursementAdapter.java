package com.artqueen.logicuniversitystationerysystem.Representative.Adapters;

/**
 * Created by shaikmdashiq on 3/3/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.artqueen.logicuniversitystationerysystem.R;
import com.artqueen.logicuniversitystationerysystem.Representative.Data.Disbursement;

import java.util.ArrayList;
import java.util.List;

public class DisbursementAdapter extends BaseAdapter implements ListAdapter {

    TextView dateTv,disbursementIDTv;
    private List<Disbursement> list = new ArrayList<Disbursement>();
    private Context context;

    public DisbursementAdapter(List<Disbursement> disbursementList, Context context)
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
            view = inflater.inflate(R.layout.disbursement_row, null);
        }
        dateTv = (TextView)view.findViewById(R.id.disbursementDateTV);
        dateTv.setText("Date: "+list.get(position).get("date"));

        disbursementIDTv = (TextView) view.findViewById(R.id.disbursementIDTV);
        disbursementIDTv.setText("Requisition Id: "+(list.get(position).get("disbursementID")));
        return view;
    }
}
