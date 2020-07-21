package com.example.ExpenseTracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText UsernameEditText;
    EditText PasswordEditText;
    EditText RetypePassEditText;
    EditText EmailEditText;
    EditText PhoneEditText;
    Button SignUpbutton;
    Context context;
    DatabaseHelper databaseHelper;
    String UsernameValue;
    String PwdValue;
    String RetypePassValue;
    String EmailValue;
    String PhoneValue;
    Boolean flag_forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context=getApplicationContext();
        UsernameEditText=findViewById(R.id.etUsername);
        PasswordEditText=findViewById(R.id.etSignupPassword);
        RetypePassEditText=findViewById(R.id.etRetypePass);
        EmailEditText=findViewById(R.id.etEmail);
        PhoneEditText=findViewById(R.id.etPhone);
        SignUpbutton=findViewById(R.id.btnSignUp);

        databaseHelper = new DatabaseHelper(this);

        Intent intentFromLogin=getIntent();
        if(intentFromLogin!=null)
        {
            flag_forgotPassword=intentFromLogin.getBooleanExtra("ForgotPassword",false);
            if(flag_forgotPassword)
            {
                    EmailEditText.setVisibility(View.INVISIBLE);
                    PhoneEditText.setVisibility(View.INVISIBLE);
                    SignUpbutton.setText("Update password!");
            }
            else
            {
                EmailEditText.setVisibility(View.VISIBLE);
                PhoneEditText.setVisibility(View.VISIBLE);
                SignUpbutton.setText("Sign me up!");
            }
        }
        else
        {
            EmailEditText.setVisibility(View.VISIBLE);
            PhoneEditText.setVisibility(View.VISIBLE);
            SignUpbutton.setText("Sign me up!");
        }

        //Validation of usernanme on tab
        UsernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(!focus)
                {
                    //Check if the user name is already taken
                   UsernameValue = UsernameEditText.getText().toString();

                    if(!flag_forgotPassword)
                    {
                        if( !IsUserNameUnique(UsernameValue))
                        {
                            UsernameEditText.setError("Username is already taken.");
                        }

                    }
                    else {
                        if( IsUserNameUnique(UsernameValue))
                        {
                            UsernameEditText.setError("Incorrect username.");
                        }

                    }

                }
            }
        });


        //One capital letter, One number, One symbol, length 6 chars
        PasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(!focus)
                {
                    PwdValue=PasswordEditText.getText().toString();
                  if(!IsPasswordValid(PwdValue))
                      PasswordEditText.setError("Must be min 4 chars with atleast one Capital letter, one Numeric , one Special Char ");
                }
            }
        });

        //Verify match of password and Retypepassword fields on tab
        RetypePassEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(!focus)
                {
                    RetypePassValue=RetypePassEditText.getText().toString();
                    if(!HasPasswordMatched(RetypePassValue))
                        RetypePassEditText.setError("Passwords do not match.");
                }
            }
        });

        //Validation of email on tab
        EmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(!focus)
                {
                    EmailValue=EmailEditText.getText().toString();

                    if(!IsEmailValid(EmailValue))
                    {
                        EmailEditText.setError("Invalid email!");
                    }
                }
            }
        });

        //Validation of phone on tab
        PhoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(!focus)
                {
                    PhoneValue=PhoneEditText.getText().toString();

                    if(!IsPhoneValid(PhoneValue))
                    {
                        PhoneEditText.setError("Invalid phone number!");
                    }
                }
            }
        });


        // Validate every field and Store data in a Hashmap , then redirect to Login Page by appropriate toast msg. If invalid credentials, then display respective toast msg.
        SignUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RetypePassValue=RetypePassEditText.getText().toString();
                UsernameValue=UsernameEditText.getText().toString();

                if(!flag_forgotPassword) {
                    PhoneValue = PhoneEditText.getText().toString();
                    EmailValue = EmailEditText.getText().toString();

                    if (IsEmailValid(EmailValue) && HasPasswordMatched(RetypePassValue) && IsUserNameUnique(UsernameValue) && IsPhoneValid(PhoneValue)) {
                        ContentValues userContentValues = new ContentValues();
                        userContentValues.put("user_name", UsernameValue);
                        userContentValues.put("password", RetypePassValue);
                        userContentValues.put("user_email", EmailValue);
                        userContentValues.put("user_mobile", PhoneValue);
                        userContentValues.put("isNewUser", 1);
                        userContentValues.put("rememberMe", 0);
                        userContentValues.put("isActive",0);

                      if( databaseHelper.insertUser(userContentValues)) {

                          Toast.makeText(context, "Successfully registered. You may Login now. ", Toast.LENGTH_LONG).show();
                          Intent LogIn = new Intent(context, LoginPageActivity.class);
                          startActivity(LogIn);
                      }
                      else
                      {
                          Toast.makeText(context, "Something went wrong. ", Toast.LENGTH_LONG).show();

                      }

                    } else {
                        Toast.makeText(context, "Enter valid credentials for SignUp", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {

                    if(HasPasswordMatched(RetypePassValue) && !IsUserNameUnique(UsernameValue))
                    {
                        databaseHelper.UpdateUserPassword(databaseHelper.getUserId(UsernameValue),RetypePassValue);
                        Toast.makeText(context, "Password has been updated! ", Toast.LENGTH_LONG).show();
                        Intent LogIn = new Intent(context, LoginPageActivity.class);
                        startActivity(LogIn);
                    }
                    else
                    {
                        Toast.makeText(context, "Enter valid credentials.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    //Check EmailPattern
    boolean IsEmailValid(String email)
    {
        Pattern EmailFormat= Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        return EmailFormat.matcher(email).find();

    }
    // Check if username is already taken
    boolean IsUserNameUnique(String username)
    {
        return databaseHelper.isUserUnique(username);
    }
    // Check if passwords matched
    boolean HasPasswordMatched(String Retypepwd)
    {
       return PasswordEditText.getText().toString().equals(Retypepwd);
    }
    //Check if the number has exactly 10 digits
    boolean IsPhoneValid(String ph)
    {
        Pattern PhoneFormat=Pattern.compile("^\\d{10}$");
        return PhoneFormat.matcher(ph).find();

    }

    /*
  ^                 # start-of-string
(?=.*[0-9])       # a digit must occur at least once
(?=.*[a-z])       # a lower case letter must occur at least once
(?=.*[A-Z])       # an upper case letter must occur at least once
(?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
(?=\\S+$)          # no whitespace allowed in the entire string
.{4,}             # anything, at least six places though
$                 # end-of-string
 */
    boolean IsPasswordValid( String Pass)
    {
        //Letters are considered as one category(be it small or Caps)==>"(?=.*[a-z])(?=.*[A-Z])" ->Hence min len must be 4
        Pattern PasswordFormat= Pattern.compile(  "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$");
        return PasswordFormat.matcher(Pass).find();
    }
}
