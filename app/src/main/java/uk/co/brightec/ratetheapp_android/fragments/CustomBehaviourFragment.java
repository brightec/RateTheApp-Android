package uk.co.brightec.ratetheapp_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.co.brightec.ratetheapp.RateTheApp;
import uk.co.brightec.ratetheapp_android.R;


public class CustomBehaviourFragment extends Fragment {

    public static CustomBehaviourFragment newInstance() {
        return new CustomBehaviourFragment();
    }

    public CustomBehaviourFragment() {
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
        View view = inflater.inflate(R.layout.fragment_custombehaviour, container, false);
        final TextView textView = (TextView) view.findViewById(R.id.textView);

        // Demo 4, Remove the OnRateChangeListener
        RateTheApp rta = (RateTheApp) view.findViewById(R.id.noAction);
        rta.setOnUserSelectedRatingListener(null);

        // Demo 5, Add a custom OnRateChangeListener
        rta = (RateTheApp) view.findViewById(R.id.customAction);

        // Initialise the text view to the current rating
        textView.setText(getString(R.string.current_rating) + " " + rta.getRating());

        // Add a custom OnUserSelectedRatingListener to update the text view
        rta.setOnUserSelectedRatingListener(new RateTheApp.OnUserSelectedRatingListener() {
            @Override
            public void onRatingChanged(RateTheApp rateTheApp, float rating) {
                textView.setText(getString(R.string.current_rating) + " " + rating);
            }
        });

        return view;
    }
}