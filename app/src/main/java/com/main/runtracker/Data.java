package com.main.runtracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.database.database.DataBase;
import com.main.database.database.Run;
import com.main.database.database.User;

import java.util.ArrayList;
import java.util.List;


public class Data extends Fragment {

    RecyclerView recyclerView;
    DataCardAdapter adapter;

    public Data() {
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
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState){
        List<Run> list = getData();

        recyclerView = (RecyclerView) getView().findViewById(R.id.data_recycler);

        getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setPrimaryNavigationFragment(this)
                    .commit();

        adapter = new DataCardAdapter(list, getView().getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));
    }

    private List<Run> getData(){

        Dashboard parent = (Dashboard) getActivity();
        User user = parent.getUser();
        Context ctx  = getActivity().getApplicationContext();
        DataBase db = DataBase.getInstance(ctx);

        return new ArrayList<>(db.getRunsByUserId(user.getId(), false));
    }

    public void runDeleted(int position){

        adapter.list.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
    }

    public void runUpdated(){adapter.notifyDataSetChanged();}

}