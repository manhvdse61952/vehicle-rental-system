package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.manhvdse61952.vrc_android.MapsActivity;
import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class activity_main_2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static List<Address> listAddress;

    SliderMotorbycle sld1;
    ViewPager viewPagerMotorbycle, viewPagerCar, viewPagerBus;
    SliderPersonalCar sld2;
    SliderTravelCar sld3;
    ImageView main_search, main_extra_search;
    EditText edtMainSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtMainSearch = (EditText)findViewById(R.id.edtMainSearch);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //Use for motorbycle slider
        viewPagerMotorbycle = (ViewPager)findViewById(R.id.viewPagerMotorbycle);
        sld1 = new SliderMotorbycle(activity_main_2.this);
        viewPagerMotorbycle.setAdapter(sld1);

        viewPagerCar = (ViewPager)findViewById(R.id.viewPagerCar);
        sld2 = new SliderPersonalCar(activity_main_2.this);
        viewPagerCar.setAdapter(sld2);

        viewPagerBus = (ViewPager)findViewById(R.id.viewPagerBus);
        sld3 = new SliderTravelCar(activity_main_2.this);
        viewPagerBus.setAdapter(sld3);

        main_search = (ImageView)findViewById(R.id.main_search);
        main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(activity_main_2.this, MainListSearch.class);
                startActivity(it);
            }
        });

        main_extra_search = (ImageView)findViewById(R.id.main_extra_search);
        main_extra_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(activity_main_2.this, MapsActivity.class);
                startActivity(it);
            }
        });


        //Search function
        readAddressJsonFile();
        edtMainSearch.setText("có " + listAddress.size() + " kết quả");

        //Toggle the actionbar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.activity_main_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Read JSON File
    public void readAddressJsonFile(){
        listAddress = new ArrayList<Address>();
        InputStream inputStream = this.getResources().openRawResource(R.raw.address);
        String json = null;

        //Read json file
        try {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Move json file to list
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jsonArray = obj.getJSONArray("address");
            for (int i= 0; i< jsonArray.length(); i++){
                JSONObject item = jsonArray.getJSONObject(i);
                Address itemTemp = new Address();
                itemTemp.setCity(item.getString("city"));
                itemTemp.setDistrict(item.getString("district"));

                listAddress.add(itemTemp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
