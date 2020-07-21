package com.example.ExpenseTracker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPageActivity extends AppCompatActivity {
    Context context;
    Button SignupBtn,loginbutton,ForgotPassButton;
    EditText txtboxUsername,txtBoxpass;
    CheckBox checkBoxRememberMe;

    Toast t;
    DatabaseHelper databaseHelper;
    Dialog myDialog;
    EditText et_annual_income;
    EditText et_Annual_savings;
    EditText et_max_daily_expense;
    Button btnclose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        context=getApplicationContext();
        loginbutton= findViewById(R.id.btnLogin);
        txtboxUsername=findViewById(R.id.txtUsername);
        txtBoxpass=findViewById(R.id.txtPassword);
        SignupBtn=findViewById(R.id.btnSignup);
        ForgotPassButton=findViewById(R.id.btnForgotPassword);
        checkBoxRememberMe=findViewById(R.id.cbRememberMe);
        databaseHelper = new DatabaseHelper(this);
        myDialog=new Dialog(this);
       Cursor UsernameNPassword= databaseHelper.getUserNameNPassword();

       checkBoxRememberMe.setChecked(false);
        if(UsernameNPassword.getCount()>0)
        {
            UsernameNPassword.moveToFirst();
            txtboxUsername.setText(UsernameNPassword.getString(1));//username
            txtBoxpass.setText(UsernameNPassword.getString(2));//password
            checkBoxRememberMe.setChecked(true);
            UsernameNPassword.close();

        }
        //If Username and password are correct then redirect to Welcome page. Else show appriate toast msg.
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = txtboxUsername.getText().toString();
                String password = txtBoxpass.getText().toString();
                int userid=databaseHelper.getUserId(username);

               if(checkBoxRememberMe.isChecked())
               {
                   databaseHelper.setRememberme(userid);
               }
               else
               {
                   databaseHelper.DontRememberme(userid);
               }
               databaseHelper.setisActive(userid);


                if(!databaseHelper.isUserUnique(username)){
                    if(databaseHelper.isLoginValid(username,password)){
                        t =Toast.makeText(context,"Login Successful.",Toast.LENGTH_LONG);


                        Intent MainScreenIntent=new Intent(context,MainScreen.class);
                        MainScreenIntent.putExtra("UserName", username);
                        startActivity(MainScreenIntent);

                    }
                    else if(password.equals("")){
                        txtBoxpass.setError("Password can't be empty!");
                    }
                    else
                    {
                        t =Toast.makeText(context,"Incorrect Password!",Toast.LENGTH_LONG);
                        t.show();
                        txtBoxpass.setText("");
                    }
                } else if (username.equals("")){
                    Toast toast =Toast.makeText(context,"Username can't be empty!",Toast.LENGTH_LONG);
                    toast.show();
                    txtboxUsername.setError("Username can't be empty!");
                    txtboxUsername.setText("");
                    txtBoxpass.setText("");
                }
                else{
                    txtboxUsername.setError("User not found!");
                    Toast toast = Toast.makeText(context, "User not found! Click SignUp to register.", Toast.LENGTH_LONG);
                    toast.show();
                    txtBoxpass.setText("");
                }

            }
        });

        // On SignUp button click , user is taken to SignUp Activity Screen
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(LoginPageActivity.this,SignupActivity.class);

                startActivity(signup);
            }
        });
        ForgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(LoginPageActivity.this,SignupActivity.class);
                signup.putExtra("ForgotPassword",true);
                startActivity(signup);

            }
        });


    }



}
