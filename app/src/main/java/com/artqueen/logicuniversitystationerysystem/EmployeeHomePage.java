package com.artqueen.logicuniversitystationerysystem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;


public class EmployeeHomePage extends ActionBarActivity {
    TextView name;
    Button makeRequest,updateRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_page);
        Intent i = getIntent();
         HashMap<String, String> b =(HashMap) i.getSerializableExtra("EmpDetails");
        name= (TextView) findViewById(R.id.nameTV);
        final String username = b.get("userName");

        final ImageView image = (ImageView) findViewById(R.id.displayIV);
        new AsyncTask<String, Void, Bitmap>() {
            Bitmap b;
            @Override
            protected Bitmap doInBackground(String... id) {
                String url=String.format(id[0]);
                try {
                    InputStream in = new URL(url).openStream();
                    b = BitmapFactory.decodeStream(in);
                } catch (Exception ex) {
                    Log.e("Bitmap Error", ex.toString());
                }
                return b;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                image.setImageBitmap(bitmap);
                name.setText("Welcome! "+username);
            }
        }.execute(b.get("userPhoto"));

        makeRequest = (Button) findViewById(R.id.makeRequestBtn);
        makeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeHomePage.this,MakeRequest.class);
                startActivity(i);
            }
        });
        updateRequest = (Button) findViewById(R.id.updateRequestBtn);
        updateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeeHomePage.this,UpdateRequisition.class);
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_employee_home_page, menu);
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
