package com.mustafa.r.hegazi.trying;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {
    Button signUp,login;
    TextView forgotPass;
    DBHelper dbHelper;
    EditText userOrEmail, password;
    CheckBox rememberMe;
    sharedString returnData;
    SwitchCompat switchCompat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }
        else {
            setTheme(R.style.Theme_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        switchCompat = findViewById(R.id.bt_switch);


        initViews();


         sharedString data = sharedPrefRead();
         String user = data.getUsername();
         String pass= data.getPassword();
         userOrEmail.setText(user);
         password.setText(pass);


        clickActions();


    }

    private void clickActions()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _username = userOrEmail.getText().toString();
                String _password = password.getText().toString();
                if (TextUtils.isEmpty(_username)|| TextUtils.isEmpty(_password))
                {
                    Toast.makeText(SignInActivity.this, "Enter you data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(dbHelper.checkUserRegistered(_username,_password))
                    {
                        if (rememberMe.isChecked())
                        {
                            sharedPrefWrite(_username,_password);

                        }
                        startActivity(new Intent(SignInActivity.this, ActionTakeActivity.class));
                    }
                    else
                    {
                        Toast.makeText(SignInActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,ForgotPassActivity.class));
            }
        });
    }

    private void sharedPrefWrite(String username, String password) {
        SharedPreferences shared = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
    }

    private void initViews() {

        dbHelper = new DBHelper(this);
        signUp = findViewById(R.id.signUpBtn);
        login = findViewById(R.id.loginBtn);
        forgotPass = findViewById(R.id.forgotPassword);
        userOrEmail = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        rememberMe = findViewById(R.id.rememberMe);
    }

    private sharedString sharedPrefRead()
    {
        returnData = new sharedString();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        returnData.setUsername(sharedPreferences.getString("username",""));
        returnData.setPassword(sharedPreferences.getString("password",""));
        return returnData;
    }
    public class sharedString
    {
        String username;
        String password;

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public sharedString() {
        }

        public String getUsername() {
            return username;
        }


        public String getPassword() {
            return password;
        }
    }
}