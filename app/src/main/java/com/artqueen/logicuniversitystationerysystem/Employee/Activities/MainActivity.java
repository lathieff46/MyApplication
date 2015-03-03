package com.artqueen.logicuniversitystationerysystem.Employee.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
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
    boolean doubleBackToExitPressedOnce;

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
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Login Failed")
                                    .setMessage("Please Try Again or Contact System Administrator")
                                    .setPositiveButton("Ok, Got It!", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        else {
                            String pwd = p.get("userPassword");

                            if (password.equals(pwd)) {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                String role = p.get("userRoleId");
                                if (role.equals("DE")) {
                                    Intent intent = new Intent(MainActivity.this, EmployeeHomePage.class);
                                    startActivity(intent);
                                }
                            } else
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Login Failed")
                                        .setMessage("Please Try Again or Contact System Administrator")
                                        .setPositiveButton("Ok, Got It!", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                        }

                    }
                }.execute(username);
            }
        });

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
