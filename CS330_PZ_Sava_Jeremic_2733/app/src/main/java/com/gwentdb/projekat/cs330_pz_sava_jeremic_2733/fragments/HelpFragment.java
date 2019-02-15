package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;

public class HelpFragment extends Fragment{

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.help, container, false);


        return view;
    }
}
