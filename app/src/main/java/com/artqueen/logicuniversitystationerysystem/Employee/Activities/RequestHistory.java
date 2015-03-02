package com.artqueen.logicuniversitystationerysystem.Employee.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.artqueen.logicuniversitystationerysystem.Employee.Data.Requisition;
import com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters.RequestHistoryAdapter;
import com.artqueen.logicuniversitystationerysystem.Employee.ListAdapters.UpdateRequisitionAdapter;
import com.artqueen.logicuniversitystationerysystem.R;

import java.util.List;

public class RequestHistory extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_history);

        String empID = MainActivity.p.get("userId");
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        List<Requisition> list = Requisition.historyList(empID);
        if(list.isEmpty())
        {
            new AlertDialog.Builder(RequestHistory.this)
                    .setTitle("Sorry, No Requests")
                    .setMessage("You have made no Request, yet !")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            RequestHistoryAdapter adapter = new RequestHistoryAdapter(list, RequestHistory.this);
            ListView lView = (ListView)findViewById(R.id.requestHistoryLV);
            lView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_history, menu);
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
