package com.artqueen.logicuniversitystationerysystem.Employee.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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

import com.artqueen.logicuniversitystationerysystem.Employee.Data.Items;
import com.artqueen.logicuniversitystationerysystem.JSONParser;
import com.artqueen.logicuniversitystationerysystem.R;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.RequisitionDetails;
import com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters.UpdateCartAdapter;
import com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters.UpdateRequisitionAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class UpdateCart extends ActionBarActivity implements AdapterView.OnItemClickListener{

    Button goBack;
    public static int RequisitionID;
    NumberPicker quantityNP;
    TextView uom;
   public static int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
//        RequisitionID=Integer.valueOf(getIntent().getStringExtra("id"));
        UpdateCartAdapter cartAdapter =new UpdateCartAdapter(UpdateRequisitionAdapter.cart,this);
        cartAdapter.RequisitionID=RequisitionID;
        ListView list = (ListView) findViewById(R.id.cartLV);
        list.setAdapter(cartAdapter);
        list.setOnItemClickListener(this);

        goBack = (Button)findViewById(R.id.addMoreBtn);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                Intent i=new Intent(UpdateCart.this,MakeRequest.class);
                i.putExtra("id",RequisitionID);
                Log.e(">>>ID:",""+RequisitionID);
                startActivity(i);

            }
        });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onItemClick(AdapterView<?> av, View v, final int position, long id) {

        Toast.makeText(UpdateCart.this, "Clicked", Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View yourCustomView = inflater.inflate(R.layout.dialog_box, null);

        quantityNP = (NumberPicker) yourCustomView.findViewById(R.id.numberPicker);
        quantityNP.setMinValue(1);
        quantityNP.setMaxValue(100);
        quantityNP.setValue(Integer.parseInt(UpdateRequisitionAdapter.cart.get(position).get("qty")));

        uom = (TextView) yourCustomView.findViewById(R.id.unitOfMeasureTv);
        uom.setText("Unit of Measure: "+UpdateRequisitionAdapter.cart.get(position).get("unitOfMeasure"));

        builder.setView(yourCustomView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String qty = String.valueOf(quantityNP.getValue());
                        Toast.makeText(UpdateCart.this,qty,Toast.LENGTH_SHORT).show();
                        UpdateRequisitionAdapter.cart.get(position).saveQty(qty);
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
                ConfirmRequisition(RequisitionID);

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setTitle("Make the Requisition")
                .show();
    }


    public void ConfirmRequisition(int RequisitionID)
    {
       RequisitionDetails.deleteRequisitionallDetial(RequisitionID);
       saveDetail(RequisitionID);
    }

    public void saveDetail(int RequisitionID)
    {
        for(Items a:UpdateRequisitionAdapter.cart)
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
                String result= JSONParser.postStream("http://10.10.1.144/Logic/Service.svc/InsertRequisitionDetail", json);
            } catch (JSONException e) {
                e.printStackTrace();

            }
            UpdateRequisitionAdapter.cart=new ArrayList<Items>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_cart, menu);
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
