package com.main.runtracker;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.main.database.database.DataBase;
import com.main.database.database.User;

public class LoginActivity extends AppCompatActivity {

    EditText email, pass;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        * GOOGLE SIGN IN OPTIONS
         */
        //TODO handle google sign in options in firebase console

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        /*
        *   LOGIN BUTTON HANDLER
         */
        loginButton = findViewById(R.id.loginButton);
        email = findViewById(R.id.editTextEmailAddress);
        pass =  findViewById(R.id.editTextPassword);


        loginButton.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v){

                String email_text = email.getText().toString();
                String pass_text = pass.getText().toString();

                Context ctx = getApplicationContext();

                DataBase db = DataBase.getInstance(ctx);

                User user = new User();

                if(validateFields()) {
                    user = db.validateUser(email_text, pass_text, ctx);
                }

                if(user.getUserName() != null)
                {
                    openDashboard(user);
                }
            }
        });

        /*
        *   Create account handler
         */
        TextView makeAccount = findViewById(R.id.create_account);
        makeAccount.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //Start a new activity that lets user write account to database
                openCreateAccount();
            }
        });

    }
    private boolean validateFields() {

        boolean isValid = true;

        if(email.length() == 0 || !(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())){
            email.setError("Please enter a valid email.");
            isValid = false;
        }

        if(pass.length() < 7) {
            pass.setError("Invalid password.");
            isValid = false;
        }

        //all fields pass
        return isValid;
    }

    public void openCreateAccount(){
        Intent i = new Intent(this, CreateAccountActivity.class);
        startActivity(i);
    }

    public void openDashboard(User user){
        Intent i = new Intent(this, Dashboard.class);
        i.putExtra("user", user.getEmail());
        startActivity(i);
        finish();
    }

}