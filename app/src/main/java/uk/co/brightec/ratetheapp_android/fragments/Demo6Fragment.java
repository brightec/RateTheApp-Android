package uk.co.brightec.ratetheapp_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.co.brightec.ratetheapp.RateTheApp;
import uk.co.brightec.ratetheapp_android.R;


public class Demo6Fragment extends Fragment {

    public static Demo6Fragment newInstance() {
        return new Demo6Fragment();
    }

    public Demo6Fragment() {
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
        View view = inflater.inflate(R.layout.fragment_demo6, container, false);
        final TextView textView = (TextView) view.findViewById(R.id.textView);

        // Add a custom OnRateChangeListener
        RateTheApp rta = (RateTheApp) view.findViewById(R.id.customAction);
        rta.setOnUserSelectedRatingListener(new RateTheApp.OnUserSelectedRatingListener() {
            @Override
            public void onRatingChanged(RateTheApp rateTheApp, float rating) {
                textView.setText("Rating: " + rating);
            }
        });
        textView.setText("Rating: "+rta.getRating());

        // Remove the OnRateChangeListener
        rta = (RateTheApp) view.findViewById(R.id.noAction);
        rta.setOnUserSelectedRatingListener(null);

        return view;
    }
}