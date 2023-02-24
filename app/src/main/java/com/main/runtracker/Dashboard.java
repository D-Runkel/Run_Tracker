package com.main.runtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.main.database.database.DataBase;
import com.main.database.database.User;

public class Dashboard extends AppCompatActivity{

    NavigationBarView bottomNavigationView;
    /*
    *   Fragment manager handles the fragments
    *   Fragments are initialized here
    *   @addRunShow is a special variable that controls the FAB state and fragment
     */
    ExtendedFloatingActionButton fab;
    FragmentManager manager = getSupportFragmentManager();
    User user;
    HomeFragment homeFragment = new HomeFragment();
    Data dataFragment = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        fab = findViewById(R.id.fab);
        showHome();
        //Grabbing the userEmail of the logged in user
        Bundle bundle = getIntent().getExtras();
        String user_email = bundle.getString("email");

        Context ctx = getApplicationContext();
        DataBase db = DataBase.getInstance(ctx);

        user = db.getUserByEmail(user_email);

        /*
        * Bottom navigational selector
         */
        bottomNavigationView = findViewById(R.id.bottomAppBar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
                        return false;
                }
            }
        });

        /*
        * FAB listener
         */
        resetFab(fab, manager);
    }



    /**
     * Sets back to default styles and icon
    * @param fab, The floating action button
     */
    public static void resetFab(ExtendedFloatingActionButton fab, FragmentManager manager) {

        fab.setIconResource(R.drawable.directions_run_fill0_wght200_grad0_opsz24);
        fab.setText(R.string.add_run);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF2596be")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().replace(R.id.fragment_container, new CreateRun())
                        .addToBackStack(null)
                        .commit();
                saveFab(fab, manager);
            }
        });
    }

    /**
     * Sets background to green and icon to save symbol
     * @param fab, the floating action button
     */
    public static void saveFab(ExtendedFloatingActionButton fab, FragmentManager manager){
        fab.setIconResource(R.drawable.save_fill1_wght200_grad0_opsz24);
        fab.setText(R.string.save);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF49a13a")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().replace(R.id.fragment_container, new CreateRun())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    /**
     * Method to pass user object to fragments
     */
    public User getUser(){
        return user;
    }

    public void showHome(){
        resetFab(fab, manager);
        manager.beginTransaction().replace(R.id.fragment_container, homeFragment)
                                .addToBackStack(null)
                                .commit();
    }

    public void showSettings(){
        //TODO inflate settings fragment
        resetFab(fab, manager);
        manager.beginTransaction().replace(R.id.fragment_container,new SettingsFragment(), "settings")
                .addToBackStack(null)
                .commit();
    }
    public void showData(){
        resetFab(fab, manager);
        manager.beginTransaction().replace(R.id.fragment_container, dataFragment, "data")
                                .addToBackStack(null)
                                .commit();
    }

}