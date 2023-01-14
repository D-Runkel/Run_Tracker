package com.main.runtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){

                }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.home:
                showHome();
                return true;
            case R.id.settings:
                showSettings();
                return true;
            case R.id.data:
                showData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showHome(){
        //TODO inflate home fragment
    }

    public void showSettings(){
        //TODO inflate settings fragment
    }
    public void showData(){
        //TODO inflate data fragment
    }
}