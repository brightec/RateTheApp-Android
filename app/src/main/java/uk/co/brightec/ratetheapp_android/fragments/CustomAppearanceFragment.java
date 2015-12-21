package uk.co.brightec.ratetheapp_android.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uk.co.brightec.ratetheapp_android.R;


public class CustomAppearanceFragment extends Fragment {

    public static CustomAppearanceFragment newInstance() {
        return new CustomAppearanceFragment();
    }

    public CustomAppearanceFragment() {
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
        View view = inflater.inflate(R.layout.fragment_customappearance, container, false);

        // Add action for View Sourcecode button
        View button = view.findViewById(R.id.btn_viewsource);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.custom_appearance_viewSourceURL);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }
}