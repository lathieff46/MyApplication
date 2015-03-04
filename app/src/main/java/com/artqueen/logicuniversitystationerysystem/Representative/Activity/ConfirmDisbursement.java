package com.artqueen.logicuniversitystationerysystem.Representative.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.artqueen.logicuniversitystationerysystem.Employee.Activities.MainActivity;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Department;
import com.artqueen.logicuniversitystationerysystem.HomePage;
import com.artqueen.logicuniversitystationerysystem.R;
import com.artqueen.logicuniversitystationerysystem.Representative.Adapters.DisbursementAdapter;
import com.artqueen.logicuniversitystationerysystem.Representative.Data.Disbursement;
import com.artqueen.logicuniversitystationerysystem.Representative.Data.DisbursementDetails;

import java.util.List;

public class ConfirmDisbursement extends ActionBarActivity implements AdapterView.OnItemClickListener{

    ListView lView;
    public static List<Disbursement> masterDisbursementList;
    public static List<DisbursementDetails> disbursementDetailsList;
    int LoadFlag = 0;
    ProgressBar confirmSpinner;
    long startTime,endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_disbursement);
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        String deptID = Department.getDepartment(MainActivity.p.get("userDepartmentId")).get("departmentid");
        confirmSpinner = (ProgressBar) findViewById(R.id.confirmSpinner);

        //TODO
        //Have to change this hardcoding
        masterDisbursementList = Disbursement.list("D1");
        if(masterDisbursementList.isEmpty())
        {
            new AlertDialog.Builder(ConfirmDisbursement.this)
                    .setTitle("Sorry, No Disbursements")
                    .setMessage("You have made new Disbursements, yet !")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(ConfirmDisbursement.this,HomePage.class));
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            DisbursementAdapter adapter = new DisbursementAdapter(masterDisbursementList, ConfirmDisbursement.this);
            lView = (ListView)findViewById(R.id.disbursementListLV);
            lView.setAdapter(adapter);
            lView.setOnItemClickListener(this);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, final int position, long id) {

        LoadFlag=1;
        loadSpinner();
        new AsyncTask<String, Void, List<DisbursementDetails>>() {
            @Override
            protected List<DisbursementDetails> doInBackground(String... params) {
                return  DisbursementDetails.list(params[0]);
            }
            @Override
            protected void onPostExecute(List<DisbursementDetails> result) {

                disbursementDetailsList = result;
                Intent i=new Intent(ConfirmDisbursement.this,DisbursementDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Position",position);
                i.putExtras(bundle);
                startActivity(i);
                LoadFlag=0;
                loadSpinner();

            }
        }.execute(masterDisbursementList.get(position).get("disbursementID"));

    }

    void loadSpinner()
    {
        if(LoadFlag==1) {
            confirmSpinner.setVisibility(View.VISIBLE);
        }
        else{
            confirmSpinner.setVisibility(View.INVISIBLE);
        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_disbursement, menu);
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
