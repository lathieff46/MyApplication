package com.artqueen.logicuniversitystationerysystem.Employee.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Department;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Items;
import com.artqueen.logicuniversitystationerysystem.HomePage;
import com.artqueen.logicuniversitystationerysystem.JSONParser;
import com.artqueen.logicuniversitystationerysystem.R;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Requisition;
import com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters.UpdateRequisitionAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class UpdateRequisition extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_requisition);
        String empID = MainActivity.p.get("userId");
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        List<Requisition> list = Requisition.list(empID);
        if(list.isEmpty())
        {
            new AlertDialog.Builder(UpdateRequisition.this)
                    .setTitle("Sorry, No Requests")
                    .setMessage("You have made no Request, yet !")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(UpdateRequisition.this,HomePage.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            UpdateRequisitionAdapter adapter = new UpdateRequisitionAdapter(list, UpdateRequisition.this);
            ListView lView = (ListView)findViewById(R.id.updateRequitionLV);
            lView.setAdapter(adapter);
        }
    }

    public void submitRequisition(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ConfirmRequisition();

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setTitle("Make the Requisition")
                .show();
    }


    public void ConfirmRequisition()
    {
        JSONObject requisition=new JSONObject();
        try{
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date one=new java.util.Date();
            String dName= Department.getDepartment(MainActivity.p.get("userDepartmentId")).get("departmentName");
            requisition.put("DepartmentName", dName);
            requisition.put("EmployeeID",MainActivity.p.get("userId"));
            requisition.put("Status","Pending");
            requisition.put("Comments",null);
            requisition.put("ProcessStatus","NotProcess");
            requisition.put("Date",sdf.format(one));
            String json=requisition.toString();
            String result= JSONParser.postStream("http://10.10.1.144/Logic/Service.svc/InsertRequisition", json);

            new AsyncTask<String,Void,Requisition>(){
                Requisition r=null;
                @Override
                protected Requisition doInBackground(String... params) {
                    try {
                        r=Requisition.GetLastRequisition(MainActivity.p.get("userId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return r;
                }
                @Override
                protected void onPostExecute(Requisition r)
                {
                    int req = Integer.valueOf(r.get("requisitionID"));
                    saveDetail(req);
                }

            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveDetail(int RequisitionID) {
        for (Items a : MakeRequest.cart) {
            String id = a.get("itemId");
            String qty = a.get("qty");
            int reqId = RequisitionID;
            JSONObject requisitionDetail = new JSONObject();
            try {
                requisitionDetail.put("ItemID", id);
                requisitionDetail.put("Qty", qty);
                requisitionDetail.put("RequisitionID", reqId);
                String json = requisitionDetail.toString();
                String result = JSONParser.postStream("http://10.10.1.144/Logic/Service.svc/InsertRequisitionDetail", json);
            } catch (JSONException e) {
                e.printStackTrace();

            }
            MakeRequest.cart = new ArrayList<Items>();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_requisition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
