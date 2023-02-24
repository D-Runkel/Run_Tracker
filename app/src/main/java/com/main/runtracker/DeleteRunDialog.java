package com.main.runtracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.main.database.database.DataBase;

public class DeleteRunDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Bundle args = getArguments();
        Long runId = args.getLong("id");
        Integer pos = args.getInt("pos");
        Context ctx  = getActivity().getApplicationContext();
        DataBase db = DataBase.getInstance(ctx);
        FragmentManager fm = getActivity().getSupportFragmentManager();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to delete this run?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteRunById(runId);
                        Data data = (Data) fm.findFragmentByTag("data");
                        data.runDeleted(pos);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close dialog
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
