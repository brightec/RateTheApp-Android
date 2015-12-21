package uk.co.brightec.ratetheapp_android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import uk.co.brightec.ratetheapp.RateTheApp;
import uk.co.brightec.ratetheapp.Utils;
import uk.co.brightec.ratetheapp_android.fragments.CustomAppearanceFragment;
import uk.co.brightec.ratetheapp_android.fragments.CustomBehaviourFragment;
import uk.co.brightec.ratetheapp_android.fragments.DefaultFragment;

/**
 * Demo app to show the RateTheApp control being used.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        initHeaderView();

        // Start with default demo
        getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, DefaultFragment.newInstance()).commit();
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

    View.OnClickListener openBrightecWebsiteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url = getString(R.string.brightecUrl);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    };

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
        resetWidget(null);

        // Reset the Custom Appearance widgets
        resetWidget("customisedTitleWidget");
        resetWidget("noTitleWidget");
        resetWidget("customisedStarColourWidget");

        // Reset the Custom Behaviour widgets
        resetWidget("noActionWidget");
        resetWidget("customActionWidget");

        // Reload the current fragment to show reset data
        Fragment demoHolder = getSupportFragmentManager().findFragmentById(R.id.demoHolder);
        if (demoHolder != null) {
            if (demoHolder instanceof DefaultFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, DefaultFragment.newInstance()).commit();
            }
            else if (demoHolder instanceof CustomAppearanceFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, CustomAppearanceFragment.newInstance()).commit();
            }
            else if (demoHolder instanceof CustomBehaviourFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, CustomBehaviourFragment.newInstance()).commit();
            }
        }
    }

    /**
     * Helper method to reset a RateTheApp widget's rating and visibility.
     * @param rateTheAppName
     */
    private void resetWidget(String rateTheAppName) {
        String instanceName = RateTheApp.PREF_RATETHEAPP_PREFIX;
        if (rateTheAppName != null) {
            instanceName += "_" + rateTheAppName;
        }
        // Reset the widget visibility to true
        Utils.saveSharedSetting(this, instanceName + RateTheApp.PREF_RATETHEAPP_SHOW_SUFFIX, true);
        // Rest the widget rating to zero
        Utils.saveSharedSetting(this, instanceName + RateTheApp.PREF_RATETHEAPP_RATING_SUFFIX, 0f);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_default) {
            getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, DefaultFragment.newInstance()).commit();
        }
        else if (id == R.id.nav_custom_appearance) {
            getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, CustomAppearanceFragment.newInstance()).commit();
        }
        else if (id == R.id.nav_custom_behaviour) {
            getSupportFragmentManager().beginTransaction().replace(R.id.demoHolder, CustomBehaviourFragment.newInstance()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}