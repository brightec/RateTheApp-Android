package uk.co.brightec.ratetheapp_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.brightec.ratetheapp_android.R;


public class Demo2Fragment extends Fragment {

    public static Demo2Fragment newInstance() {
        return new Demo2Fragment();
    }

    public Demo2Fragment() {
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
        return inflater.inflate(R.layout.fragment_demo2, container, false);
    }
}