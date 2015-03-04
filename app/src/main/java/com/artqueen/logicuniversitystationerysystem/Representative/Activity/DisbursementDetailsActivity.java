package com.artqueen.logicuniversitystationerysystem.Representative.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.artqueen.logicuniversitystationerysystem.Employee.Activities.MainActivity;
import com.artqueen.logicuniversitystationerysystem.Employee.Activities.MakeRequest;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Users;
import com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters.CartAdapter;
import com.artqueen.logicuniversitystationerysystem.R;
import com.artqueen.logicuniversitystationerysystem.Representative.Adapters.DisbursementAdapter;
import com.artqueen.logicuniversitystationerysystem.Representative.Adapters.DisbursementDetailsAdapter;
import com.artqueen.logicuniversitystationerysystem.Representative.Data.DisbursementDetails;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class DisbursementDetailsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    ListView lView;
    NumberPicker quantityNP;
    TextView uom,disbursementID,clerkName,disbursementDate;
    public static List<DisbursementDetails> DetailsList = ConfirmDisbursement.disbursementDetailsList;
    private ProgressBar spinner,loadSpinner;
    Users a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_details);
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        loadSpinner = (ProgressBar) findViewById(R.id.loadSpinner);


        DisbursementDetailsAdapter adapter = new DisbursementDetailsAdapter(DetailsList, DisbursementDetailsActivity.this);
        lView = (ListView)findViewById(R.id.disbursementDetailsLV);
        lView.setAdapter(adapter);
        lView.setOnItemClickListener(this);

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("Position");

        disbursementID = (TextView) findViewById(R.id.disbursementIDinDetailsTV);
        disbursementID.setText(DetailsList.get(position).get("disbursementID"));

        clerkName = (TextView) findViewById(R.id.disbursementClerkName);
        String clerkID = ConfirmDisbursement.masterDisbursementList.get(position).get("clerkID");
        Log.e(">>ClerkID",""+clerkID);
         a = Users.getUserByID(clerkID);
        String clerkname = a.get("userName");
        clerkName.setText(clerkname);

        disbursementDate = (TextView) findViewById(R.id.disbursementDetailsDate);
        disbursementDate.setText(ConfirmDisbursement.masterDisbursementList.get(position).get("date"));

        spinner = (ProgressBar)findViewById(R.id.clerkIVProgress);
        final ImageView image = (ImageView) findViewById(R.id.clerkIV);
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
            }
        }.execute(a.get("userPhoto"));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadSpinner.setVisibility(View.GONE);
            }
        }, 1500);



    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View yourCustomView = inflater.inflate(R.layout.dialog_box, null);

        quantityNP = (NumberPicker) yourCustomView.findViewById(R.id.numberPicker);
        quantityNP.setMinValue(0);
        quantityNP.setMaxValue(100);
        quantityNP.setValue(Integer.parseInt(DisbursementDetailsAdapter.pendingQuantity));

        uom = (TextView) yourCustomView.findViewById(R.id.unitOfMeasureTv);
        uom.setVisibility(View.INVISIBLE);



        builder.setView(yourCustomView)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String qty = String.valueOf(quantityNP.getValue());
                        ConfirmDisbursement.disbursementDetailsList.get(position).put("pendingAmount",qty);
                        DisbursementDetailsAdapter.pendingQty.setText("Pending Quantity: "+qty);

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
        getMenuInflater().inflate(R.menu.menu_disbursement_details, menu);
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
