/*
 * Copyright 2016 Brightec Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.brightec.ratetheapp_android.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uk.co.brightec.ratetheapp.RateTheApp;
import uk.co.brightec.ratetheapp_android.R;


public class CustomBehaviourFragment extends Fragment {

    public CustomBehaviourFragment() {
        // Required empty public constructor
    }

    public static CustomBehaviourFragment newInstance() {
        return new CustomBehaviourFragment();
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

        // Add action for View Sourcecode button
        View button = view.findViewById(R.id.btn_viewsource);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.custom_behaviour_viewSourceURL);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        // Demo 4, Remove the OnRateChangeListener
        RateTheApp rta = (RateTheApp) view.findViewById(R.id.noAction);
        rta.setOnUserSelectedRatingListener(null);

        // Demo 5, Add a custom OnRateChangeListener
        rta = (RateTheApp) view.findViewById(R.id.customAction);

        // Initialise the text view to the current rating
        final TextView textView = (TextView) view.findViewById(R.id.textView);
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