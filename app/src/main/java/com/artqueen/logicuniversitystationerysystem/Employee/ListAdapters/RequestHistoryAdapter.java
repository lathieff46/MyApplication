package com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Items;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Requisition;
import com.artqueen.logicuniversitystationerysystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaikmdashiq on 2/3/15.
 */
public class RequestHistoryAdapter extends BaseAdapter implements ListAdapter {
    TextView dateTv,requisitionNum,requestStatus;
    private List<Requisition> list = new ArrayList<Requisition>();
    private Context context;
    public static List<Items> cart=new ArrayList<Items>();

    public RequestHistoryAdapter(List<Requisition> requisitionList, Context context)
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
            view = inflater.inflate(R.layout.request_history_row, null);
        }
        dateTv = (TextView)view.findViewById(R.id.requestHistoryDate);
        dateTv.setText("Date: "+list.get(position).get("Date"));

        requisitionNum = (TextView) view.findViewById(R.id.requestHistoryID);
        requisitionNum.setText("Requisition Id: "+(list.get(position).get("requisitionID")));

        requestStatus = (TextView)view.findViewById(R.id.requestHistoryStatus);
        requestStatus.setText("Status: "+list.get(position).get("Status"));

        return view;
    }
}