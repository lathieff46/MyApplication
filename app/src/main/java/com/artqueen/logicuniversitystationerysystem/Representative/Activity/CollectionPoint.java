package com.artqueen.logicuniversitystationerysystem.Representative.Activity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.artqueen.logicuniversitystationerysystem.Employee.Activities.MainActivity;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Department;
import com.artqueen.logicuniversitystationerysystem.HomePage;
import com.artqueen.logicuniversitystationerysystem.JSONParser;
import com.artqueen.logicuniversitystationerysystem.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CollectionPoint extends ActionBarActivity {

    TextView departmentName,currentCollectionPoint,newCollectionPoint;
    NumberPicker collectionPicker;
    Button cancelBtn,saveBtn;
    String newCP ="Management School" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collection_point);
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);

        departmentName = (TextView) findViewById(R.id.departmentNameTV);
        String departName = Department.getDepartment(MainActivity.p.get("userDepartmentId")).get("departmentName");
        departmentName.setText(departName);

        currentCollectionPoint = (TextView) findViewById(R.id.currentCollectionPoitnTV);
        final String collectionPoint = Department.getDepartment(MainActivity.p.get("userDepartmentId")).get("collectionpoint");
        currentCollectionPoint.setText(collectionPoint);

        collectionPicker = (NumberPicker) findViewById(R.id.collectionPicker);
        newCollectionPoint = (TextView) findViewById(R.id.newCollectionPointTV);

        final String[] values = new String[6];
        values[0] = "Management School";
        values[1] = "Medical School";
        values[2] = "Engineering School";
        values[3] = "Science School";
        values[4] = "University Hospital";
        values[5] = "Stationery Store";
        collectionPicker.setMaxValue(values.length - 1);
        collectionPicker.setMinValue(0);
        collectionPicker.setDisplayedValues(values);
        collectionPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        collectionPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newCP = values[newVal];
                    newCollectionPoint.setText("New Collection Point: "+newCP);
            }
        });

        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CollectionPoint.this, HomePage.class));
            }
        });

        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        JSONObject collectionDetail=new JSONObject();
                        try{
                            String id= Department.getDepartment(MainActivity.p.get("userDepartmentId")).get("departmentid");
                            String collection = newCP;
                            collectionDetail.put("DepartmentID",id);
                            collectionDetail.put("CollectionPoint", collection);
                            String json=collectionDetail.toString();
                            String result= JSONParser.postStream("http://10.10.1.144/Logic/Service.svc/ChangeCollectionPoint", json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                Toast.makeText(CollectionPoint.this,"Collection Point changed to "+newCP,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CollectionPoint.this,HomePage.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collection_point, menu);
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
