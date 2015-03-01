package com.artqueen.logicuniversitystationerysystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.List;


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
