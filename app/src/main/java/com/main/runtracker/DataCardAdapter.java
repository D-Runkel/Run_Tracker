package com.main.runtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.main.database.database.Run;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class DataCardAdapter extends RecyclerView.Adapter<RunDataViewHolder> {

    List<Run> list;
    Context context;
    private RunDataViewHolder holder;
    private int position;

    public DataCardAdapter(List<Run> list, Context context){
        this.context = context;
        this.list = list;
    }

    @Override
    public RunDataViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType){

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View dataView = inflater.inflate(R.layout.data_card,parent,false);

        return new RunDataViewHolder(dataView);
    }

    @Override
    public void
    onBindViewHolder(final RunDataViewHolder holder, @SuppressLint("RecyclerView") final int position){

        LocalDate date = list.get(position).getRunDate();
        String dateTextString = Integer.toString(date.getMonthValue()) + "-"
                + Integer.toString(date.getDayOfMonth()) +"-"
                + Integer.toString(date.getYear());

        holder.dateText.setText(dateTextString);

        String distanceString = Float.toString(list.get(position).getMiles()) + " MI";

        holder.distanceText.setText(distanceString);

        String timeString = list.get(position).humanReadableFormatTime();
        holder.paceText.setText(timeString);

        FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putLong("id", list.get(position).getId());
                args.putInt("pos", position);
                DialogFragment newFrag = new DeleteRunDialog();
                newFrag.setArguments(args);

                newFrag.show(manager, "TAG");
            }
        });

        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putLong("id", list.get(position).getId());
                args.putString("date", dateTextString);
                args.putInt("pos", position);
                CreateRun createRun = new CreateRun();
                createRun.setArguments(args);
                manager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_container, createRun)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }



}
