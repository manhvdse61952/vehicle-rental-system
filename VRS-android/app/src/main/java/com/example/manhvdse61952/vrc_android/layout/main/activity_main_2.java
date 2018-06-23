package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class activity_main_2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView main_search, main_extra_search;
    EditText edtMainSearch;

    ImmutableValue locationObj = new ImmutableValue();

    ///////////////// USE FOR TAB LAYOUT /////////
    public static ViewPager viewPager;
    private TabLayout tabLayout;
    public static SectionPageAdapter secAdapter;
    /////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //////////////////////// USE FOR TABLAYOUT /////////////////
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
        /////////////////////////////////////////////////////////////
        edtMainSearch = (EditText) findViewById(R.id.edtMainSearch);
        main_search = (ImageView) findViewById(R.id.main_search);
        main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationObj.checkAddressPermission(activity_main_2.this, activity_main_2.this);
                edtMainSearch.setText(locationObj.district + ", " + locationObj.city);
            }
        });


        main_extra_search = (ImageView) findViewById(R.id.main_extra_search);
        main_extra_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent it = new Intent(activity_main_2.this, MapsActivity.class);
//                startActivity(it);

                /////////////////// USE FOR TEST SEARCH //////////////////////
                new SimpleSearchDialogCompat(activity_main_2.this, "", "Nhập đia điểm cần kiếm xe", null, initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        Toast.makeText(activity_main_2.this, "" + searchable.getTitle(), Toast.LENGTH_SHORT).show();
                        if (!searchable.getTitle().equals("")) {
                            edtMainSearch.setText("" + searchable.getTitle());
                        }
                        baseSearchDialogCompat.dismiss();
                    }
                }).show();
            }
        });

        //Toggle the actionbar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //Use for nav layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        View hView = navigationView.getHeaderView(0);
        TextView txtNavFullname = (TextView)hView.findViewById(R.id.txtNavFullname);
        TextView txtNavUsername = (TextView)hView.findViewById(R.id.txtNavUsername);
        TextView txtNavRole = (TextView)hView.findViewById(R.id.txtNavRole);
        Toast.makeText(this,editor.getString("roleName", "ROLE_USER") , Toast.LENGTH_SHORT).show();
        txtNavUsername.setText(editor.getString("usernameAfterLogin", "Empty"));
        if (editor.getString("roleName", "ROLE_USER").equals("ROLE_USER")) {
            txtNavRole.setText("Khách hàng");
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_manage_vehicle).setVisible(false);
            nav_Menu.findItem(R.id.nav_manage_drivers).setVisible(false);
            nav_Menu.findItem(R.id.nav_discount).setVisible(false);
        } else {
            txtNavRole.setText("Chủ xe");
        }

    }

    /////////////// USE for TAB Layout/////////
    private void createTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Xe máy");
        tabOne.setTextSize(15);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_motorbike, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Ô tô cá nhân");
        tabTwo.setTextSize(15);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_car_green, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Ô tô du lịch");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bus_green, 0, 0);
        tabThree.setTextSize(15);
        tabLayout.getTabAt(2).setCustomView(tabThree);

    }

    private void setupViewPager(ViewPager viewPager) {
        secAdapter = new SectionPageAdapter(getSupportFragmentManager());
        secAdapter.addFragment(new tab1(), "Xe máy");
        secAdapter.addFragment(new tab2(), "Ô tô cá nhân");
        secAdapter.addFragment(new tab3(), "Ô tô du lịch");
        viewPager.setAdapter(secAdapter);
    }


    ///////////////////////////////////////////////////////////////


    //////////////////////////////////// USE FOR SEARCH //////////////////////////////////
    private ArrayList<SearchAddressModel> initData() {
        ArrayList<SearchAddressModel> items = new ArrayList<>();
        items.add(new SearchAddressModel("quan 1 ho chi minh"));
        items.add(new SearchAddressModel("quan 2 ho chi minh"));
        items.add(new SearchAddressModel("quan 3 ho chi minh"));
        items.add(new SearchAddressModel("quan 4 ho chi minh"));
        items.add(new SearchAddressModel("quan go vap ho chi minh"));
        items.add(new SearchAddressModel("quan 5 ho chi minh"));
        return items;
    }
    //////////////////////////////////////////////////////////////////////////////////////

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
        getMenuInflater().inflate(R.menu.activity_main_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

        if (id == R.id.nav_logout) {
            Intent it = new Intent(activity_main_2.this, LoginActivity.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
