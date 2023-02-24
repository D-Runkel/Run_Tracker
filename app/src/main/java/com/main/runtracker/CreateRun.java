package com.main.runtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.main.database.database.DataBase;
import com.main.database.database.Run;
import com.main.database.database.User;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class CreateRun extends Fragment {


    public CreateRun() {
        // Required empty public constructor
    }
    Boolean update = false;
    Button edit_date;
    TextView date_window, title;
    ImageButton exit_create_run;
    EditText hours_et, mins_et, secs_et, distance_et;
    int rec_position;
    User user;
    Run run_to_update = new Run();
    /*
     * Instance of fragment manager
     */
    FragmentManager fragmentManager;

    /*
     * Instance of calendar
     */

    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));


    /*
     *   Setting the text in our date window on load
     */
    String[] dateString = { (calendar.get(Calendar.MONTH)+1)
            + "-" + calendar.get(Calendar.DATE) + "-"
            + calendar.get(Calendar.YEAR)};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_run, container, false);
        Dashboard dashboard = (Dashboard)getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        user = dashboard.getUser();
        Dashboard.saveFab(getActivity().findViewById(R.id.fab), fragmentManager);


        Long runId = null;
        String dateText = null;
        if(getArguments() != null) {
            runId = getArguments().getLong("id");
            dateText = getArguments().getString("date");
            rec_position = getArguments().getInt("pos");
        }


        if(runId != null){
            update = true;
            run_to_update = DataBase.getInstance(getActivity().getApplicationContext()).getRunById(runId);
        }

        //Allows fragment to remove itself in sub-method
        Fragment fragment = this;

        /*
        *   Grabbing views
         */
        edit_date = view.findViewById(R.id.add_date);
        date_window = view.findViewById(R.id.date_window);
        exit_create_run = view.findViewById(R.id.exit_create_run);
        distance_et = view.findViewById(R.id.distance_et);
        hours_et = view.findViewById(R.id.pace_hours);
        mins_et = view.findViewById(R.id.pace_mins);
        secs_et = view.findViewById(R.id.pace_secs);
        title = view.findViewById(R.id.title_create_run);

        /*
        * Building the date picker
         */
        MaterialDatePicker<Long> datePicker;
        if(!update) {
            date_window.setText(dateString[0]);
            datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
        }
        else{

            Long hours = run_to_update.getRunTime().toHours();
            Long mins = run_to_update.getRunTime().toMinutes() % 60;
            hours_et.setText(String.format("%d", hours));
            mins_et.setText(String.format("%d", mins));
            secs_et.setText(Long.toString((run_to_update.getRunTime().toSeconds() % 60)));

            distance_et.setText(Float.toString(run_to_update.getMiles()));
            date_window.setText(dateText);
            title.setText(R.string.update_run);


            Instant instant = run_to_update.getRunDate().atStartOfDay(ZoneId.of("UTC")).toInstant();
            long timeInMilis = instant.toEpochMilli();

            datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Update date")
                    .setSelection(timeInMilis)
                    .build();
        }
        /*
        * Date picker onSelection listener
         */
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                calendar.setTimeInMillis(selection);

                dateString[0] = (calendar.get(Calendar.MONTH) + 1)
                        + "-" + calendar.get(Calendar.DATE) + "-"
                        + calendar.get(Calendar.YEAR);

                date_window.setText(dateString[0]);
            }
        });

        /*
        * Edit date button pops out the calendar
         */
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateString[0] = (calendar.get(Calendar.MONTH) + 1)
                        + "-" + calendar.get(Calendar.DATE) + "-"
                        + calendar.get(Calendar.YEAR);

                datePicker.show(fragmentManager, "datePicker");
                date_window.setText(dateString[0]);
            }
        });

        /*
        *  FAB listener for this view
        */
        ExtendedFloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context ctx = getActivity().getApplicationContext();
                //convert to local date
                LocalDate localDate = toLocalDate(calendar.getTime());

                //Load inputs into object
                Duration duration = Duration.ZERO;

                duration = duration.plusHours(getLongFromEt(hours_et));
                duration = duration.plusMinutes(getLongFromEt(mins_et));
                duration = duration.plusSeconds(getLongFromEt(secs_et));

                if(getLongFromEt(hours_et) == 0 && getLongFromEt(mins_et) == 0 && getLongFromEt(secs_et)==0){
                    Toast.makeText(ctx,"Enter a time greater than 0.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(distance_et.length() == 0){
                    Toast.makeText(ctx,"Please enter a distance!",Toast.LENGTH_SHORT).show();
                    return;
                }


                float distance = Float.valueOf(distance_et.getText().toString());

                DataBase db = DataBase.getInstance(ctx);

                //hash an id
                long runId = Objects.hash(duration , distance , user.getId(), localDate);

                Run run = new Run(runId, user.getId(), distance,0, duration, localDate.plusDays(1));

                if(update){

                    //id never changes
                    run_to_update.setMiles(distance);
                    run_to_update.setRunDate(localDate.plusDays(1));
                    run_to_update.setRunTime(duration);

                    if (!db.updateRun(run_to_update)) {
                        return;
                    }
                    Toast.makeText(ctx,"Run Updated!", Toast.LENGTH_SHORT).show();
                    Dashboard.resetFab(fab,fragmentManager);
                    Data data_frag = (Data) getActivity().getSupportFragmentManager().findFragmentByTag("data");

                    data_frag.runUpdated();

                    getActivity().onBackPressed();

                }else{
                    if(db.addRun(run)) {
                        Toast.makeText(ctx,"Run Added!", Toast.LENGTH_SHORT).show();
                        Dashboard.resetFab(fab,fragmentManager);
                        getActivity().onBackPressed();
                    };
                }
            }
        });

        /*
         * Create a button to allow the user to back out if needed.
         */
        exit_create_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dashboard.resetFab(fab, fragmentManager);
                getActivity().onBackPressed();

            }
        });
        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Dashboard dashboard = (Dashboard) getActivity();
        ExtendedFloatingActionButton fab = dashboard.findViewById(R.id.fab);
        Dashboard.resetFab(fab, fragmentManager);
    }

    @Override
    public void onResume(){
        super.onResume();


    }

    /**
     * Converts time to local date
     * @param date, the date
     * @return Local date
     */
    public LocalDate toLocalDate(Date date){

        ZoneId zoneId = ZoneId.systemDefault();
        return date.toInstant().atZone(zoneId).toLocalDate();
    }

    public Long getLongFromEt(EditText et){

        if(et.length()==0) {
            return Long.parseLong("0");
        }else{
            return Long.parseLong(et.getText().toString());
        }
    }
}