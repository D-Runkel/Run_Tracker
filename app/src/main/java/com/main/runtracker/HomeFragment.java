package com.main.runtracker;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.main.database.database.DataBase;
import com.main.database.database.Run;
import com.main.database.database.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class HomeFragment extends Fragment {

    Button test_sms;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState){

        renderChart();
        test_sms = getView().findViewById(R.id.test_sms);

        test_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = getContext();

                if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.SEND_SMS) == PermissionChecker.PERMISSION_DENIED){
                    requestPermissions(new String[] {Manifest.permission.SEND_SMS}, 100 );
                }

                if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.SEND_SMS) == PermissionChecker.PERMISSION_GRANTED) {
                    String address = ctx.getSharedPreferences("root_preferences", Context.MODE_PRIVATE).getString("phone-number", "0");
                    SmsManager smsManager = SmsManager.getDefault();

                    smsManager.sendTextMessage(address, address, "SMS Test", null, null);
                }

            }
        });

    }

    public void renderChart(){
        /*
         *   Loading data into chart
         */
        BarChart chart = (BarChart) getView().findViewById(R.id.chart);

        Dashboard parent = (Dashboard) getActivity();
        Context ctx = parent.getApplicationContext();

        DataBase db = DataBase.getInstance(ctx);

        User user = parent.getUser();
        Vector<Run> runs = db.getRunsByUserId(user.getId(), true);
        //Vector<Run> conditioned = conditionData(runs);
        List<BarEntry> entries = new ArrayList<>();

        int xValue = 0;

        for(Run run: runs ){
            entries.add(new BarEntry((float)xValue,run.getMiles()));
            xValue++;
        }

        BarDataSet dataSet = new BarDataSet(entries,"Miles");
        BarData barData = new BarData(dataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        chart.setData(barData);
        chart.setFitBars(true);
        chart.invalidate();
    }

}