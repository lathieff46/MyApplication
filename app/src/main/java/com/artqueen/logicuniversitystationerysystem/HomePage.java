package com.artqueen.logicuniversitystationerysystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.artqueen.logicuniversitystationerysystem.Employee.Activities.MainActivity;
import com.artqueen.logicuniversitystationerysystem.Employee.Activities.MakeRequest;
import com.artqueen.logicuniversitystationerysystem.Employee.Activities.RequestHistory;
import com.artqueen.logicuniversitystationerysystem.Employee.Activities.UpdateRequisition;
import com.artqueen.logicuniversitystationerysystem.Representative.Activity.CollectionPoint;
import com.artqueen.logicuniversitystationerysystem.Representative.Activity.ConfirmDisbursement;
import com.artqueen.logicuniversitystationerysystem.Representative.Data.Disbursement;

import java.io.InputStream;
import java.net.URL;


public class HomePage extends ActionBarActivity {
    TextView name;
    Button makeRequest,updateRequest,requestHistory,disbursementList,collectionPoint;
    private ProgressBar spinner;
    boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        name= (TextView) findViewById(R.id.nameTV);
        final String username = MainActivity.p.get("userName");
        String userRole = MainActivity.p.get("userRoleId");
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        final ImageView image = (ImageView) findViewById(R.id.displayIV);
        new AsyncTask<String, Void, Bitmap>() {
            Bitmap b;
            @Override
            protected Bitmap doInBackground(String... id) {
                spinner.setVisibility(View.VISIBLE);
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
                spinner.setVisibility(View.GONE);
                image.setImageBitmap(bitmap);
                name.setText("Welcome! "+username);
            }
        }.execute(MainActivity.p.get("userPhoto"));

        if(userRole.equals("DR"))
        {
            disbursementList = (Button) findViewById(R.id.disbursementListBtn);
            disbursementList.setVisibility(View.VISIBLE);
            collectionPoint = (Button) findViewById(R.id.collectionPointBtn);
            collectionPoint.setVisibility(View.VISIBLE);
        }

        disbursementList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, ConfirmDisbursement.class));
            }
        });

        collectionPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, CollectionPoint.class));
            }
        });

        makeRequest = (Button) findViewById(R.id.makeRequestBtn);
        makeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this,MakeRequest.class);
                startActivity(i);
            }
        });
        updateRequest = (Button) findViewById(R.id.updateRequestBtn);
        updateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePage.this,UpdateRequisition.class);
                startActivity(i);
            }
        });

        requestHistory = (Button) findViewById(R.id.stationeryRequestHistoryBtn);
        requestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(HomePage.this,RequestHistory.class));
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
            new AlertDialog.Builder(HomePage.this)
                    .setTitle("Logout!")
                    .setMessage("Are you sure you want to Logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(HomePage.this,MainActivity.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
