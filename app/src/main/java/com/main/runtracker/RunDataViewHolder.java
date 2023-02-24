package com.main.runtracker;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RunDataViewHolder extends RecyclerView.ViewHolder {

    TextView dateText;
    TextView distanceText;
    TextView paceText;
    View view;
    Button edit_button;
    Button delete_button;

    RunDataViewHolder(View itemView){
        super(itemView);

        edit_button = (Button) itemView.findViewById(R.id.edit_run);
        delete_button = (Button) itemView.findViewById(R.id.delete_run);
        dateText = (TextView) itemView.findViewById(R.id.date_on_card);
        distanceText = (TextView) itemView.findViewById(R.id.distance_on_card);
        paceText = (TextView) itemView.findViewById(R.id.pace_on_card);

        view = itemView;

    }
}
