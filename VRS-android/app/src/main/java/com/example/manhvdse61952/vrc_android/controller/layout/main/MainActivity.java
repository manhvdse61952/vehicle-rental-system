package com.example.manhvdse61952.vrc_android.controller.layout.main;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.account.UpdateAccount;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ContractDetail;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage.UpdateVehicle;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail.VehicleDetail;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.ContractAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.contract.ManageContractActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage.ManageVehicleActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showlist.MotobikeTab;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showlist.CarTab;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showlist.TravelCarTab;
import com.example.manhvdse61952.vrc_android.model.api_model.City;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;
import com.example.manhvdse61952.vrc_android.model.api_model.District;
import com.example.manhvdse61952.vrc_android.model.search_model.SearchVehicleItem;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView img_current_address, main_extra_search;
    TextView txt_main_search_address, txt_longitude, txt_latitude;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    ProgressDialog dialog;
    LinearLayout ln_advance;
    Spinner spn_vehicleType, spn_seat, spn_priceType, spn_city, spn_district;
    EditText edt_priceFrom, edt_priceTo;
    int cityPosition = 0, districtID = 0;
    RelativeLayout rl_around;

    Boolean isOpenMaker = false, isOpenAdvance = false;
    int fromPrice = 0, toPrice = 0;
    ///////////////// USE FOR SEARCH ADAPTER ////////
    public static List<SearchVehicleItem> listMotorbike = new ArrayList<>();
    public static List<SearchVehicleItem> listPersonalCar = new ArrayList<>();
    public static List<SearchVehicleItem> listTravelCar = new ArrayList<>();
    //////////////////////////////////////////////////////////////////////

    ///////////////// USE FOR TAB LAYOUT /////////
    public static ViewPager viewPager;
    private TabLayout tabLayout;
    public static SectionPageAdapter secAdapter;
    /////////////////////////////////////////////////////////////

    ///////////////// TEST CODE ///////////
    LocationManager locationManager;
    LocationListener locationListener;
    ///////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        declareID();

        checkScreen();

        getCurrentLocation(true);

        GeneralController.scaleView(ln_advance, 0);

        //Place Autocomplete
        showSearchPlace();

        //Init layout in the first time
        getAllVehicleByDistrictID(44);

        img_current_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
                        "Đang xử lý", true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isOpenMaker == false){
                            getCurrentLocation(false);
                            img_current_address.setImageResource(R.drawable.ic_map_marker_alt);
                            Toast.makeText(MainActivity.this, "Đã bật tự động lấy vị trí", Toast.LENGTH_SHORT).show();
                            isOpenMaker = true;
                        } else if (isOpenMaker){
                            locationManager.removeUpdates(locationListener);
                            img_current_address.setImageResource(R.drawable.ic_map_disable);
                            Toast.makeText(MainActivity.this, "Đã tắt tự động lấy vị trí", Toast.LENGTH_SHORT).show();
                            isOpenMaker = false;
                        }
                        dialog.dismiss();
                    }
                }, 500);
            }
        });

        main_extra_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenAdvance == false){
                    GeneralController.scaleView(ln_advance, 600);
                    isOpenAdvance = true;
                } else if (isOpenAdvance == true){
                    GeneralController.scaleView(ln_advance, 0);
                    isOpenAdvance = false;
                }
            }
        });


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
        secAdapter.addFragment(new MotobikeTab(), "Xe máy");
        secAdapter.addFragment(new CarTab(), "Ô tô cá nhân");
        secAdapter.addFragment(new TravelCarTab(), "Ô tô du lịch");
        viewPager.setAdapter(secAdapter);
    }

    ////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_2, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_check_contract) {
            Intent it = new Intent(MainActivity.this, ManageContractActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_manage_vehicle) {
            Intent it = new Intent(MainActivity.this, ManageVehicleActivity.class);
            startActivity(it);

//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
        } else if (id == R.id.nav_manage_account) {
            Intent it = new Intent(MainActivity.this, UpdateAccount.class);
            startActivity(it);
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất ?").setCancelable(false)
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences settings = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            settings.edit().clear().commit();
                            SharedPreferences settings_3 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            settings_3.edit().clear().commit();
                            MainActivity.this.finish();
                            Intent it = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(it);
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }
        return true;
    }

    private void declareID(){
        img_current_address = (ImageView)findViewById(R.id.img_current_address);
        txt_main_search_address = (TextView)findViewById(R.id.txt_main_search_address);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_extra_search = (ImageView) findViewById(R.id.main_extra_search);
        txt_longitude = (TextView)findViewById(R.id.txt_longitude);
        txt_latitude = (TextView)findViewById(R.id.txt_latitude);
        ln_advance = (LinearLayout)findViewById(R.id.ln_advance);
        spn_vehicleType = (Spinner)findViewById(R.id.spn_vehicleType);
        spn_city = (Spinner)findViewById(R.id.spn_city);
        spn_district = (Spinner)findViewById(R.id.spn_district);
        spn_priceType = (Spinner)findViewById(R.id.spn_priceType);
        spn_seat = (Spinner)findViewById(R.id.spn_seat);
        edt_priceFrom = (EditText)findViewById(R.id.edt_priceFrom);
        edt_priceTo = (EditText)findViewById(R.id.edt_priceTo);
        rl_around = (RelativeLayout)findViewById(R.id.rl_around);

        //Toggle the actionbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (GeneralAPI.listAddressFromDB.size() == 0) {
            GeneralAPI testAPI = new GeneralAPI();
            testAPI.getAllAddress(MainActivity.this);
        }
        //Use for nav layout
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        View hView = navigationView.getHeaderView(0);
        TextView txtNavFullname = (TextView) hView.findViewById(R.id.txtNavFullname);
        TextView txtNavUsername = (TextView) hView.findViewById(R.id.txtNavUsername);
        TextView txtNavRole = (TextView) hView.findViewById(R.id.txtNavRole);
        txtNavUsername.setText(editor.getString(ImmutableValue.HOME_username, "Empty"));
        txtNavFullname.setText(editor.getString(ImmutableValue.HOME_fullName, "Empty"));
        if (editor.getString(ImmutableValue.HOME_role, ImmutableValue.ROLE_USER).equals(ImmutableValue.ROLE_USER)) {
            txtNavRole.setText("Khách hàng");
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_manage_vehicle).setVisible(false);
            nav_Menu.findItem(R.id.nav_manage_drivers).setVisible(false);
            nav_Menu.findItem(R.id.nav_discount).setVisible(false);
        } else {
            txtNavRole.setText("Chủ xe");
        }

        rl_around.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
                        "Đang xử lý", true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent it = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(it);
                        dialog.dismiss();
                    }
                }, 500);
            }
        });

        List<String> listVehicleType = new ArrayList<>();
        listVehicleType.add("máy");
        listVehicleType.add("cá nhân");
        listVehicleType.add("du lịch");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, listVehicleType);
        spn_vehicleType.setAdapter(adapter1);

        List<String> listSeat = new ArrayList<>();
        listSeat.add("2");
        listSeat.add("4-7");
        listSeat.add("8-16");
        listSeat.add("17-32");
        listSeat.add("> 32");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, listSeat);
        spn_seat.setAdapter(adapter2);

        List<String> listPriceType = new ArrayList<>();
        listPriceType.add("ngày");
        listPriceType.add("giờ");
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, listPriceType);
        spn_priceType.setAdapter(adapter3);

        if (GeneralAPI.listAddressFromDB.size() != 0){
            initDistrictAndCity();
        }

        edt_priceFrom.addTextChangedListener(convertFromPriceRealTime(edt_priceFrom));
        edt_priceTo.addTextChangedListener(convertToPriceRealTime(edt_priceTo));
    }

    //Use for init layout

    private int getDistrictIdByName(String districtName) {
        List<City> listCity = GeneralAPI.listAddressFromDB;
        int districtID = 0;
        for (int i = 0; i < listCity.size(); i++) {
            List<District> listDistrict = listCity.get(i).getDistrict();
            for (int j = 0; j < listDistrict.size(); j++) {
                String districtConvert = listDistrict.get(j).getDistrictName().toLowerCase().replace("quận", "");
                if (districtConvert.equals(districtName)){
                    districtID = listDistrict.get(j).getId();
                    break;
                }

            }
        }
        return districtID;
    }

    private void getAllVehicleByDistrictID(int districtID) {
        dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
                "Đang xử lý", true);
        Retrofit test = RetrofitConfig.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<SearchVehicleItem>> responseBodyCall = testAPI.getVehicleByDistrict(districtID);
        responseBodyCall.enqueue(new Callback<List<SearchVehicleItem>>() {
            @Override
            public void onResponse(Call<List<SearchVehicleItem>> call, Response<List<SearchVehicleItem>> response) {
                if (response.code() == 200) {
                    if (response != null) {
                        listMotorbike = new ArrayList<>();
                        listPersonalCar = new ArrayList<>();
                        listTravelCar = new ArrayList<>();
                        List<SearchVehicleItem> listAll = response.body();
                        for (int i = 0; i < listAll.size(); i++) {
                            if (listAll.get(i).getVehicleType().equals(ImmutableValue.XE_MAY)) {
                                SearchVehicleItem searchObj = new SearchVehicleItem();
                                searchObj.setSeat(listAll.get(i).getSeat());
                                searchObj.setFrameNumber(listAll.get(i).getFrameNumber());
                                searchObj.setDistrictID(listAll.get(i).getDistrictID());
                                searchObj.setRentFeePerHours(listAll.get(i).getRentFeePerHours());
                                searchObj.setVehicleMaker(listAll.get(i).getVehicleMaker());
                                searchObj.setCurrentStatus(listAll.get(i).getCurrentStatus());
                                searchObj.setVehicleModel(listAll.get(i).getVehicleModel());
                                searchObj.setImageLinkFront(listAll.get(i).getImageLinkFront());
                                searchObj.setDiscountValue(listAll.get(i).getDiscountValue());
                                searchObj.setRentFeePerDay(listAll.get(i).getRentFeePerDay());
                                searchObj.setVehicleType(listAll.get(i).getVehicleType());
                                listMotorbike.add(searchObj);
                            } else if (listAll.get(i).getVehicleType().equals(ImmutableValue.XE_CA_NHAN)) {
                                SearchVehicleItem searchObj = new SearchVehicleItem();
                                searchObj.setSeat(listAll.get(i).getSeat());
                                searchObj.setFrameNumber(listAll.get(i).getFrameNumber());
                                searchObj.setDistrictID(listAll.get(i).getDistrictID());
                                searchObj.setRentFeePerHours(listAll.get(i).getRentFeePerHours());
                                searchObj.setVehicleMaker(listAll.get(i).getVehicleMaker());
                                searchObj.setCurrentStatus(listAll.get(i).getCurrentStatus());
                                searchObj.setVehicleModel(listAll.get(i).getVehicleModel());
                                searchObj.setImageLinkFront(listAll.get(i).getImageLinkFront());
                                searchObj.setDiscountValue(listAll.get(i).getDiscountValue());
                                searchObj.setRentFeePerDay(listAll.get(i).getRentFeePerDay());
                                searchObj.setVehicleType(listAll.get(i).getVehicleType());
                                listPersonalCar.add(searchObj);
                            } else if (listAll.get(i).getVehicleType().equals(ImmutableValue.XE_DU_LICH)) {
                                SearchVehicleItem searchObj = new SearchVehicleItem();
                                searchObj.setSeat(listAll.get(i).getSeat());
                                searchObj.setFrameNumber(listAll.get(i).getFrameNumber());
                                searchObj.setDistrictID(listAll.get(i).getDistrictID());
                                searchObj.setRentFeePerHours(listAll.get(i).getRentFeePerHours());
                                searchObj.setVehicleMaker(listAll.get(i).getVehicleMaker());
                                searchObj.setCurrentStatus(listAll.get(i).getCurrentStatus());
                                searchObj.setVehicleModel(listAll.get(i).getVehicleModel());
                                searchObj.setImageLinkFront(listAll.get(i).getImageLinkFront());
                                searchObj.setDiscountValue(listAll.get(i).getDiscountValue());
                                searchObj.setRentFeePerDay(listAll.get(i).getRentFeePerDay());
                                searchObj.setVehicleType(listAll.get(i).getVehicleType());
                                listTravelCar.add(searchObj);
                            }
                        }
                        viewPager = (ViewPager) findViewById(R.id.container);
                        setupViewPager(viewPager);
                        tabLayout = (TabLayout) findViewById(R.id.tabs);
                        tabLayout.setupWithViewPager(viewPager);
                        createTabIcons();
                        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        int tabIndex = editor.getInt(ImmutableValue.HOME_tabIndex, 0);
                        viewPager.setCurrentItem(tabIndex);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    viewPager = (ViewPager) findViewById(R.id.container);
                    setupViewPager(viewPager);
                    tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);
                    createTabIcons();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<SearchVehicleItem>> call, Throwable t) {
                viewPager = (ViewPager) findViewById(R.id.container);
                setupViewPager(viewPager);
                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);
                createTabIcons();
                Toast.makeText(MainActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void initDistrictAndCity() {
        if (GeneralAPI.listAddressFromDB.size() == 0){
            GeneralAPI testAPI = new GeneralAPI();
            testAPI.getAllAddress(MainActivity.this);
        } else {
            final List<City> listAddress = GeneralAPI.listAddressFromDB;
            ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, listAddress);
            spn_city.setAdapter(cityArrayAdapter);

            spn_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    List<District> districts = listAddress.get(position).getDistrict();
                    cityPosition = position;
                    ArrayAdapter<District> districtAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_item, districts);
                    spn_district.setAdapter(districtAdapter);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            spn_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    districtID = 0;
                    District district = listAddress.get(cityPosition).getDistrict().get(position);
                    districtID = district.getId();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private TextWatcher convertFromPriceRealTime(final EditText edt) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (!originalString.equals("")){
                        fromPrice = Integer.parseInt(originalString);
                    }


                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edt.setText(formattedString);
                    edt.setSelection(edt.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edt.addTextChangedListener(this);
            }
        };
    }

    private TextWatcher convertToPriceRealTime(final EditText edt) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edt.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    if (!originalString.equals("")){
                        toPrice = Integer.parseInt(originalString);
                    }

                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    edt.setText(formattedString);
                    edt.setSelection(edt.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                edt.addTextChangedListener(this);
            }
        };
    }

    //Use for location

    private void getCurrentLocation(final Boolean isFirstTime){
        /////////// TEST CODE ////////
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                txt_longitude.setText(longitude + "");
                txt_latitude.setText(latitude + "");
                String currentAddress = PermissionDevice.getStringAddress(longitude, latitude, MainActivity.this);
                txt_main_search_address.setText(currentAddress);
                if (isFirstTime == true){
                    locationManager.removeUpdates(locationListener);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        //////////////////////////////////////////////////////////////////////////
    }

    private void showSearchPlace(){
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String currentAddressStr = PermissionDevice.getStringAddress(place.getLatLng().longitude, place.getLatLng().latitude, MainActivity.this);
                String addressFull = "";
                if (!currentAddressStr.trim().equals("")){
                    String[] arrayAddressTemp = currentAddressStr.split(",");
                    addressFull = arrayAddressTemp[0];
                    for (int i = 1; i < arrayAddressTemp.length - 1;i++){
                        addressFull = addressFull + ", " + arrayAddressTemp[i].trim();
                    }
                }
                txt_longitude.setText(place.getLatLng().longitude + "");
                txt_latitude.setText(place.getLatLng().latitude + "");
                txt_main_search_address.setText(addressFull);
            }

            @Override
            public void onError(Status status) {
                Log.i("VRSPlace", "An error occurred: " + status);
            }
        });
    }

    private void checkScreen(){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int usernameID = editor.getInt(ImmutableValue.HOME_userID, 0);
        String vehicleFrameNumber = editor2.getString(ImmutableValue.MAIN_vehicleID, "Empty");
        String isUpdateVehicle = editor2.getString(ImmutableValue.MAIN_isUpdateVehicle, "Empty");
        String contractID = editor2.getString(ImmutableValue.MAIN_contractID, "Empty");

        if (usernameID != 0 && vehicleFrameNumber.equals("Empty") && contractID.equals("Empty") && isUpdateVehicle.equals("Empty")){
            //Nothing to do
        } else if (usernameID != 0 && !vehicleFrameNumber.equals("Empty") && contractID.equals("Empty") && isUpdateVehicle.equals("Empty")){
            Intent it = new Intent(MainActivity.this, VehicleDetail.class);
            startActivity(it);
        } else if (usernameID != 0 && !vehicleFrameNumber.equals("Empty") && contractID.equals("Empty") && !isUpdateVehicle.equals("Empty")){
            Intent it = new Intent(MainActivity.this, UpdateVehicle.class);
            startActivity(it);
        } else if (usernameID != 0 && vehicleFrameNumber.equals("Empty") && !contractID.equals("Empty")){
            dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
                    "Đang xử lý", true);
            Retrofit retrofit = RetrofitConfig.getClient();
            final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
            Call<ContractItem> responseBodyCall = contractAPI.findContractByID(contractID);
            responseBodyCall.enqueue(new Callback<ContractItem>() {
                @Override
                public void onResponse(Call<ContractItem> call, Response<ContractItem> response) {
                    if (response.code() == 200){
                        ContractItem obj = response.body();
                        String contractStatus = obj.getContractStatus();
                        if (contractStatus.equals(ImmutableValue.CONTRACT_INACTIVE)
                                || contractStatus.equals(ImmutableValue.CONTRACT_ACTIVE)
                                || contractStatus.equals(ImmutableValue.CONTRACT_FINISHED)){
                            Intent it = new Intent(MainActivity.this, ContractDetail.class);
                            startActivity(it);
                        } else {
                            Intent it = new Intent(MainActivity.this, ManageContractActivity.class);
                            startActivity(it);
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        MainActivity.this.finish();
                        Intent it = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(it);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ContractItem> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            MainActivity.this.finish();
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
        }
    }

}
