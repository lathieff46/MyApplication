package com.artqueen.logicuniversitystationerysystem.Employee.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artqueen.logicuniversitystationerysystem.R;
import com.artqueen.logicuniversitystationerysystem.Employee.Data.Users;


public class MainActivity extends ActionBarActivity {

    Button loginBtn;
    EditText userNameEt,passwordEt;
    static Users p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userNameEt = (EditText)findViewById(R.id.userNameET);
        passwordEt = (EditText)findViewById(R.id.passwordET);
        loginBtn = (Button) findViewById(R.id.loginBTN);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = userNameEt.getText().toString();
                final String password = passwordEt.getText().toString();
                new AsyncTask<String, Void, Users>() {
                    @Override
                    protected Users doInBackground(String... params) {
                         p = Users.getUser(params[0]);
                        return p;
                    }
                    @Override
                    protected void onPostExecute(Users p) {
                        if(p==null){
                            Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String pwd = p.get("userPassword");

                            if (password.equals(pwd)) {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                String role = p.get("userRoleId");
                                if (role.equals("DE")) {
                                    Intent intent = new Intent(MainActivity.this, EmployeeHomePage.class);
                                    intent.putExtra("EmpDetails", p);
                                    startActivity(intent);
                                }
                            } else
                                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }.execute(username);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
