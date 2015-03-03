package com.artqueen.logicuniversitystationerysystem.Employee.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters.CartAdapter;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Department;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Items;
import com.artqueen.logicuniversitystationerysystem.JSONParser;
import com.artqueen.logicuniversitystationerysystem.R;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Requisition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;



public class Cart extends ActionBarActivity implements AdapterView.OnItemClickListener{

    Button goBack,cancelRequest;

    NumberPicker quantityNP;
    TextView uom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ListView list = (ListView) findViewById(R.id.cartLV);
        list.setOnItemClickListener(this);

        cancelRequest = (Button) findViewById(R.id.cancelRequest);
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Cart.this)
                        .setTitle("Cancel Request")
                        .setMessage("Are you sure you want to Cancel Request ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                             Toast.makeText(Cart.this,"Request Cancelled",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Cart.this,EmployeeHomePage.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        goBack = (Button)findViewById(R.id.addMoreBtn);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this,MakeRequest.class));
            }
        });
        Log.e(">>",""+MakeRequest.cart.size());
        for(int i =0;i<MakeRequest.cart.size();i++)
        {
            Log.e(">>",""+MakeRequest.cart.get(i).get("description"));
        }
            CartAdapter cartAdapter =new CartAdapter(MakeRequest.cart,this);
            list.setAdapter(cartAdapter);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onItemClick(AdapterView<?> av, View v, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View yourCustomView = inflater.inflate(R.layout.dialog_box, null);

        quantityNP = (NumberPicker) yourCustomView.findViewById(R.id.numberPicker);
        quantityNP.setMinValue(1);
        quantityNP.setMaxValue(100);
        quantityNP.setValue(Integer.parseInt(MakeRequest.cart.get(position).get("qty")));



        uom = (TextView) yourCustomView.findViewById(R.id.unitOfMeasureTv);
        uom.setText("Unit of Measure: "+MakeRequest.cart.get(position).get("unitOfMeasure"));

        builder.setView(yourCustomView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String qty = String.valueOf(quantityNP.getValue());
                        MakeRequest.cart.get(position).saveQty(qty);
                        CartAdapter.listItemText1.setText(qty);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
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
                }).setTitle("Submit the Stationery Request for Approval ?")
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
        Toast.makeText(Cart.this,"Stationery Request Made Successfully",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,EmployeeHomePage.class));
    }

    public void saveDetail(int RequisitionID)
    {
        for(Items a:MakeRequest.cart)
        {
            String id=a.get("itemId");
            String qty=a.get("qty");
            int reqId = RequisitionID;
            JSONObject requisitionDetail=new JSONObject();
            try{
                requisitionDetail.put("ItemID",id);
                requisitionDetail.put("Qty",qty);
                requisitionDetail.put("RequisitionID",reqId);
                String json=requisitionDetail.toString();
                String result=JSONParser.postStream("http://10.10.1.144/Logic/Service.svc/InsertRequisitionDetail",json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MakeRequest.cart=new ArrayList<Items>();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
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

    @Override
    public void onBackPressed() {

    }
}
