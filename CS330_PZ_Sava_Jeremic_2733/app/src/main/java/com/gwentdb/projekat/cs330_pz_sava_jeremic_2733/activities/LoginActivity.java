package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.helper.InputValidation;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.sql.DBHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private DBHelper dbHelper = new DBHelper(this);
    private NestedScrollView nestedScrollView;

    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;

    private TextInputEditText textEmail;
    private TextInputEditText textPassword;

    private AppCompatButton buttonLogin;
    private AppCompatTextView registerLink;

    private InputValidation inputValidation;
    private DBHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews(){
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        layoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        layoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        buttonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        registerLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewRegisterLink);
    }

    private void initListeners(){
        buttonLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);
    }

    private void initObjects(){
        DBHelper = new DBHelper(this);
        inputValidation = new InputValidation(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appCompatButtonLogin:
                login();
                break;
            case R.id.appCompatTextViewRegisterLink:
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    public void login(){
        if(!inputValidation.isInputEditTextFilled(textEmail, layoutEmail, getString(R.string.error_message_email))){
            return;
        }
        if(!inputValidation.isInputEditTextEmail(textEmail, layoutEmail, getString(R.string.error_message_email))){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textPassword, layoutPassword, getString(R.string.error_message_password))){
            return;
        }

        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if(DBHelper.checkUser(email, password)){
            //SaveSharedPreference.setUserName(getApplicationContext(), email);
            Intent mainIntent = new Intent(this, MainActivity.class);
            String username = dbHelper.getUserByEmail(email);
            mainIntent.putExtra("NAME", username);
            mainIntent.putExtra("EMAIL", email);
            mainIntent.putExtra("PASSWORD", password);
            emptyInputEditText();
            startActivity(mainIntent);
        } else{
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    public void emptyInputEditText(){
        textEmail.setText(null);
        textPassword.setText(null);
    }
}
