package com.artqueen.logicuniversitystationerysystem;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;



public class Cart extends ActionBarActivity implements AdapterView.OnItemClickListener{
    List<Items> cartDetails;
    List<CartItems> cartItems;
    Button goBack;
    public static SharedPreferences savedData;
    NumberPicker quantityNP;
    TextView uom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedData= PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ListView list = (ListView) findViewById(R.id.cartLV);
        list.setOnItemClickListener(this);
        goBack = (Button)findViewById(R.id.addMoreBtn);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this,MakeRequest.class));
            }
        });

            CartAdapter cartAdapter =new CartAdapter(MakeRequest.cart,this);
            list.setAdapter(cartAdapter);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onItemClick(AdapterView<?> av, View v, final int position, long id) {

        Toast.makeText(Cart.this,"Clicked",Toast.LENGTH_LONG).show();

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
                        Toast.makeText(Cart.this,qty,Toast.LENGTH_SHORT).show();
                        MakeRequest.cart.get(position).saveQty(qty);
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
            String dName=Department.getDepartment(MainActivity.p.get("userDepartmentId")).get("departmentName");
            Toast.makeText(this,dName,Toast.LENGTH_SHORT).show();
            requisition.put("DepartmentName", dName);
            requisition.put("EmployeeID",MainActivity.p.get("userId"));
            requisition.put("Status","pendding");
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
                    Log.e(">>onpostexcute",""+req);
                    saveDetail(req);
                }

            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveDetail(int RequisitionID)
    {
        for(Items a:MakeRequest.cart)
        {
            Log.e(">>",""+RequisitionID);
            String id=a.get("itemId");
            String qty=a.get("qty");
            int reqId = RequisitionID;
            JSONObject requisitionDetail=new JSONObject();
            try{
                requisitionDetail.put("ItemID",id);
                requisitionDetail.put("Qty",qty);
                requisitionDetail.put("RequisitionID",reqId);
                String json=requisitionDetail.toString();
                Toast.makeText(this,id,Toast.LENGTH_SHORT).show();
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
