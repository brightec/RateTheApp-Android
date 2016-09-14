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

package uk.co.brightec.ratetheapp_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import uk.co.brightec.ratetheapp.DefaultOnUserSelectedRatingListener;
import uk.co.brightec.ratetheapp.InstanceSettings;
import uk.co.brightec.ratetheapp.RateTheApp;
import uk.co.brightec.ratetheapp_android.fragments.CustomAppearanceFragment;
import uk.co.brightec.ratetheapp_android.fragments.CustomBehaviourFragment;
import uk.co.brightec.ratetheapp_android.fragments.DefaultFragment;

/**
 * Demo app to show the RateTheApp control being used.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final float MIN_GOOD_RATING = 3.0f;
    View.OnClickListener openBrightecWebsiteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = getString(R.string.brightecUrl);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    };
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        initHeaderView();

        initDemoRatingWidget();

        // Start with default demo
        getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, DefaultFragment.newInstance()).commit();
    }

    /**
     * Helper method to setup a rating widget for the demo app with customised text
     */
    private void initDemoRatingWidget() {
        RateTheApp demoRating = (RateTheApp) findViewById(R.id.demoRating);
        demoRating.setOnUserSelectedRatingListener(
                new DefaultOnUserSelectedRatingListener(
                        MIN_GOOD_RATING,
                        getString(uk.co.brightec.ratetheapp.R.string.ratetheapp_negative_button),
                        getString(uk.co.brightec.ratetheapp.R.string.ratetheapp_positive_button),
                        getString(R.string.demo_goodrating_title),
                        getString(R.string.demo_goodrating_text),
                        getString(uk.co.brightec.ratetheapp.R.string.ratetheapp_badrating_title),
                        getString(R.string.demo_badrating_text),
                        getString(R.string.demo_feedback_emailaddress),
                        getString(R.string.demo_feedback_subject),
                        null));
    }

    private void initHeaderView() {
        // Inflating header view programmatically due to xml bug in Support Libraries
        View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header_main);

        // Add listener for opening Brightec Website
        View developer = headerView.findViewById(R.id.developer);
        if (developer != null) {
            developer.setOnClickListener(openBrightecWebsiteListener);
        }

        View website = headerView.findViewById(R.id.website);
        if (website != null) {
            website.setOnClickListener(openBrightecWebsiteListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_resetdemo) {
            resetDemoData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to reset the demo data (widget ratings and visiblity)
     */
    private void resetDemoData() {
        // Reset the default widget
        InstanceSettings.getInstanceSettings().resetWidget();

        // Reset the Custom Appearance widgets
        InstanceSettings.getInstanceSettings("customisedTitleWidget").resetWidget();
        InstanceSettings.getInstanceSettings("noTitleWidget").resetWidget();
        InstanceSettings.getInstanceSettings("customisedStarColourWidget").resetWidget();

        // Reset the Custom Behaviour widgets
        InstanceSettings.getInstanceSettings("noActionWidget").resetWidget();
        InstanceSettings.getInstanceSettings("customActionWidget").resetWidget();

        // Reload the current fragment to show reset data
        Fragment demoHolder = getSupportFragmentManager().findFragmentById(R.id.demoHolder);
        if (demoHolder != null) {
            if (demoHolder instanceof DefaultFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, DefaultFragment.newInstance()).commit();
            } else if (demoHolder instanceof CustomAppearanceFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, CustomAppearanceFragment.newInstance()).commit();
            } else if (demoHolder instanceof CustomBehaviourFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, CustomBehaviourFragment.newInstance()).commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_default) {
            getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, DefaultFragment.newInstance()).commit();
        } else if (id == R.id.nav_custom_appearance) {
            getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, CustomAppearanceFragment.newInstance()).commit();
        } else if (id == R.id.nav_custom_behaviour) {
            getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, CustomBehaviourFragment.newInstance()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}