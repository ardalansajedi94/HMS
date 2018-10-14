package ir.hotelsys.androidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

import ir.hotelsys.androidapp.SQLiteDB.DatabaseHandler;

public class GuestActivity extends AppCompatActivity {

    private static final String TAG = GuestActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SharedPreferences user_detail;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DatabaseHandler(GuestActivity.this);
        setContentView(R.layout.activity_main);
        user_detail=getSharedPreferences(Constants.USER_DETAIL, Context.MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        Fragment fragment = new WelcomeSlidesFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();
        MenuItem about_hotel_item=menu.findItem(R.id.about_hotel);
        MenuItem about_city_item=menu.findItem(R.id.about_city);
        MenuItem hotel_news_item=menu.findItem(R.id.hotel_news);
        MenuItem login_register_item=menu.findItem(R.id.login_register);
        MenuItem settings_item=menu.findItem(R.id.settings);
        about_hotel_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"about_hotel"));
        about_city_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"about_city"));
        hotel_news_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"hotel_news"));
        login_register_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"login_register"));
        settings_item.setTitle(db.getTranslationForLanguage(user_detail.getInt(Constants.LANGUAGE_ID,1),"settings"));
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                drawerLayout.bringToFront();
                drawerLayout.requestLayout();
            }
        };
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                    Fragment fragment;
                    FragmentTransaction fragmentTransaction;
                    switch (id) {
                        case R.id.about_hotel:
                            fragment =TabsFragment.newInstance(2);
                            break;
                        case R.id.about_city:
                            fragment =TabsFragment.newInstance(1);

                            break;
                        case R.id.hotel_news:
                            fragment = new HotelNewsFragment();
                            break;
                        case R.id.login_register:
                            fragment = new LoginRegistrationFragment();
                            break;
                        case R.id.settings:
                            fragment = new GuestSettingsFragment();
                            break;
                        default:
                            fragment = new WelcomeSlidesFragment();
                    }

                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                drawerLayout.closeDrawer(Gravity.START,true);
                return true;
            }
        });

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }
    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }
}


