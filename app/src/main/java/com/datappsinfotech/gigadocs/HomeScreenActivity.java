package com.datappsinfotech.gigadocs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.datappsinfotech.gigadocs.database.TestDb;
import com.datappsinfotech.gigadocs.fragments.AppointmentBookingFragment;
import com.datappsinfotech.gigadocs.fragments.CalenderFragment;
import com.datappsinfotech.gigadocs.fragments.LogoutFragment;
import com.datappsinfotech.gigadocs.fragments.PatientFragment;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsAPIConstants;
import com.datappsinfotech.gigadocs.utils.gigadocsutils.GigaDocsSharedPreferenceManager;
import com.datappsinfotech.gigadocs.widgets.UserSessionManager;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentTransaction fragmentTransaction;
    Fragment fragment;
    public static String TOKEN;
    TestDb myTDb= new TestDb(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TOKEN =  GigaDocsSharedPreferenceManager.getKey(getApplicationContext(), GigaDocsAPIConstants.TOKEN,null);
        super.onCreate(savedInstanceState);
        try {
            try {
                setContentView(R.layout.activity_home_screen);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                if (drawer != null) {
                    drawer.addDrawerListener(toggle);
                }
                toggle.syncState();
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                if (navigationView != null) {
                    navigationView.setNavigationItemSelectedListener(this);
                }
                UserSessionManager session = new UserSessionManager(getApplicationContext());
                if(session.checkLogin())
                    finish();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer, new PatientFragment());
                fragmentTransaction.commit();
                setTitle("Patient");

                if (navigationView != null) {
                    navigationView.setNavigationItemSelectedListener(this);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        try {
            switch (id){
                case R.id.patient:
                    fragment = new PatientFragment();
                    break;
                case R.id.calender:
                    fragment = new CalenderFragment();
                    break;
                case R.id.appointment:
                    fragment = new AppointmentBookingFragment();
                    break;
                case R.id.logout:
                    fragment = new LogoutFragment();
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.mainContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            item.setChecked(true);
            setTitle(item.getTitle());
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer != null) {
                drawer.closeDrawer(GravityCompat.START);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

}
