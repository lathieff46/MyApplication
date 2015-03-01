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
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;


public class Cart extends ActionBarActivity implements AdapterView.OnItemClickListener{
    List<Items> cartDetails;
    Button goBack;
    public static SharedPreferences savedData;
    EditText quantityEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedData= PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ListView list = (ListView) findViewById(R.id.cartLV);
        list.setOnItemClickListener(this);
        goBack = (Button)findViewById(R.id.addMoreBtn);
        Intent i = getIntent();
        cartDetails = (List<Items>) i.getSerializableExtra("cartDetails");
        CartAdapter cartAdapter =new CartAdapter(cartDetails,this);
        list.setAdapter(cartAdapter);
        //registerForContextMenu(list);
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, final int position, long id) {
        Toast.makeText(Cart.this,"Clicked",Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View yourCustomView = inflater.inflate(R.layout.dialog_box, null);

        quantityEt = (EditText) yourCustomView.findViewById(R.id.quantityEt);
        if(((HashMap<String,String>)cartDetails.get(position)).get("qty")!=null)
        {
            quantityEt.setText(((HashMap<String,String>)cartDetails.get(position)).get("qty"));
        }
        builder.setView(yourCustomView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String qty = quantityEt.getText().toString();
                        Toast.makeText(Cart.this,qty,Toast.LENGTH_SHORT).show();
                        ((HashMap<String,String>)cartDetails.get(position)).put("qty",qty);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.cartLV) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            String[] menuItems = {"Remove from Cart"};
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
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
super.onBackPressed();
    }
}
