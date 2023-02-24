package com.main.runtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.main.database.database.DataBase;

public class CreateAccountActivity extends AppCompatActivity {

    EditText name,email,password,password_verify;
    Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        createAccountButton = (Button)findViewById(R.id.createAccountButton);
        name = findViewById(R.id.create_account_name);
        email = findViewById(R.id.create_account_email);
        password = findViewById(R.id.create_account_password);
        password_verify = findViewById(R.id.create_account_password_verify);

        createAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name_text = name.getText().toString();
                String email_text = email.getText().toString();
                String password_text = password.getText().toString();

                if(validateFields()) {

                    Context ctx = getApplicationContext();
                    DataBase db = DataBase.getInstance(ctx);

                    //if user already has account
                    if(db.getUserByEmail(email_text).getUserName() != null){
                        Toast.makeText(ctx, "You already have an account!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (db.createUser(email_text, name_text, password_text)) {
                        Toast.makeText(ctx, "Account created!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ImageButton b_back = findViewById(R.id.b_back);

        b_back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private boolean validateFields() {

        boolean isValid = true;

        if(name.length() == 0 ){
            name.setError("Required Field");
            isValid = false;
        }

        if(email.length() == 0 || !(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())){
            email.setError("Please enter a valid email.");
            isValid = false;
        }

        if(password.length() < 7) {
            password.setError("Please enter a password of at least 6 characters");
            isValid = false;
        }

        if(!password_verify.getText().toString().equals(password.getText().toString())){
            password_verify.setError("Passwords must match");
            isValid = false;
        }

        //all fields pass
        return isValid;
    }

}