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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Button;
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
import com.example.manhvdse61952.vrc_android.controller.layout.promotion.PromotionActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.report.ViewReportActivity;
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
    TextView txt_main_search_address;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    int cityPosition = 0, districtID = 0;
    FloatingActionButton fab_renew;
    int fromPrice = 0, toPrice = 0, checkLoop = -1;
    List<Integer> listSeat = new ArrayList<>();
    String vehicleType = "";
    int priceType = 0;


    LinearLayout ln_search_advanced, ln_search_advanced_show, ln_around;
    Spinner spn_vehicleType, spn_seat, spn_priceType;
    EditText edt_priceFrom, edt_priceTo;
    Spinner spn_city, spn_district;
    Button btn_searchAdvance;
    Boolean isOpenAdvanced = false;

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

        checkScreen();

        declareID();

        getCurrentLocation(true);

        //Place Autocomplete
        showSearchPlace();

        //Init layout in the first time
        getAllVehicleByDistrictID(44);

        img_current_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
                        "Đang xử lý", true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            getCurrentLocation(true);
                            Toast.makeText(MainActivity.this, "Đã lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                }, 500);
            }
        });

        main_extra_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.END);
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
        } else if (id == R.id.nav_discount) {
            startActivity(new Intent(MainActivity.this, PromotionActivity.class));
        } else if (id == R.id.nav_view_history) {
            startActivity(new Intent(MainActivity.this, ViewReportActivity.class));
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

    private void declareID() {
        img_current_address = (ImageView) findViewById(R.id.img_current_address);
        txt_main_search_address = (TextView) findViewById(R.id.txt_main_search_address);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        main_extra_search = (ImageView) findViewById(R.id.main_extra_search);
        fab_renew = (FloatingActionButton)findViewById(R.id.fab_renew);

        ln_search_advanced = (LinearLayout)findViewById(R.id.ln_search_advanced);
        ln_search_advanced_show= (LinearLayout)findViewById(R.id.ln_search_advanced_show);
        spn_vehicleType = (Spinner)findViewById(R.id.spn_vehicleType);
        spn_seat = (Spinner)findViewById(R.id.spn_seat);
        edt_priceFrom = (EditText)findViewById(R.id.edt_priceFrom);
        edt_priceTo = (EditText)findViewById(R.id.edt_priceTo);
        spn_city = (Spinner)findViewById(R.id.spn_city);
        spn_district = (Spinner)findViewById(R.id.spn_district);
        spn_priceType = (Spinner)findViewById(R.id.spn_priceType);
        btn_searchAdvance = (Button)findViewById(R.id.btn_searchAdvance);
        ln_around = (LinearLayout)findViewById(R.id.ln_around);


        //Toggle the actionbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

        ln_around.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
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

        GeneralController.scaleView(ln_search_advanced_show, 0);

        ln_search_advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenAdvanced){
                    GeneralController.scaleView(ln_search_advanced_show, 0);
                    isOpenAdvanced = false;
                } else {
                    GeneralController.scaleView(ln_search_advanced_show, 700);
                    isOpenAdvanced = true;
                }
            }
        });

        List<String> listVehicleType = new ArrayList<>();
        listVehicleType.add("xe máy");
        listVehicleType.add("xe cá nhân");
        listVehicleType.add("xe du lịch");
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

        initDistrictAndCity();

        edt_priceFrom.addTextChangedListener(convertFromPriceRealTime(edt_priceFrom));
        edt_priceTo.addTextChangedListener(convertToPriceRealTime(edt_priceTo));

        fab_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllVehicleByDistrictID(44);
            }
        });

        btn_searchAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAdvancedAction();
            }
        });
    }

    //Use for init layout

    private int getDistrictIdByName(String districtName) {
        List<City> listCity = ImmutableValue.listGeneralAddress;
        int districtID = 0;
        for (int i = 0; i < listCity.size(); i++) {
            List<District> listDistrict = listCity.get(i).getDistrict();
            for (int j = 0; j < listDistrict.size(); j++) {
                String districtConvert = listDistrict.get(j).getDistrictName().toLowerCase().replace("quận", "");
                if (districtConvert.equals(districtName)) {
                    districtID = listDistrict.get(j).getId();
                    break;
                }

            }
        }
        return districtID;
    }

    private void getAllVehicleByDistrictID(int districtID) {
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
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

        final List<City> listAddress = ImmutableValue.listGeneralAddress;
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
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
                    if (!originalString.equals("")) {
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
                    if (!originalString.equals("")) {
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

    private void getCurrentLocation(final Boolean isFirstTime) {
        final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
                "Đang xử lý", true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                String currentAddress = PermissionDevice.getStringAddress(longitude, latitude, MainActivity.this);
                txt_main_search_address.setText(currentAddress);
                if (isFirstTime == true) {
                    locationManager.removeUpdates(locationListener);
                }
                dialog.dismiss();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            dialog.dismiss();
            finish();
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        //////////////////////////////////////////////////////////////////////////
    }

    private void showSearchPlace() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        EditText edtPlace = (EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);
        edtPlace.setHint("Nhập địa chỉ...");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String currentAddressStr = PermissionDevice.getStringAddress(place.getLatLng().longitude, place.getLatLng().latitude, MainActivity.this);
                String addressFull = "";
                if (!currentAddressStr.trim().equals("")) {
                    String[] arrayAddressTemp = currentAddressStr.split(",");
                    addressFull = arrayAddressTemp[0];
                    for (int i = 1; i < arrayAddressTemp.length - 1; i++) {
                        addressFull = addressFull + ", " + arrayAddressTemp[i].trim();
                    }
                }
                txt_main_search_address.setText(addressFull);
            }

            @Override
            public void onError(Status status) {
                Log.i("VRSPlace", "An error occurred: " + status);
            }
        });
    }

    private void checkScreen() {
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int usernameID = editor.getInt(ImmutableValue.HOME_userID, 0);
        String vehicleFrameNumber = editor2.getString(ImmutableValue.MAIN_vehicleID, "Empty");
        String isUpdateVehicle = editor2.getString(ImmutableValue.MAIN_isUpdateVehicle, "Empty");
        String contractID = editor2.getString(ImmutableValue.MAIN_contractID, "Empty");

        if (usernameID != 0 && vehicleFrameNumber.equals("Empty") && contractID.equals("Empty") && isUpdateVehicle.equals("Empty")) {
            //Nothing to do
        } else if (usernameID != 0 && !vehicleFrameNumber.equals("Empty") && contractID.equals("Empty") && isUpdateVehicle.equals("Empty")) {
            Intent it = new Intent(MainActivity.this, VehicleDetail.class);
            startActivity(it);
        } else if (usernameID != 0 && !vehicleFrameNumber.equals("Empty") && contractID.equals("Empty") && !isUpdateVehicle.equals("Empty")) {
            Intent it = new Intent(MainActivity.this, UpdateVehicle.class);
            startActivity(it);
        } else if (usernameID != 0 && vehicleFrameNumber.equals("Empty") && !contractID.equals("Empty")) {
            final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
                    "Đang xử lý", true);
            Retrofit retrofit = RetrofitConfig.getClient();
            final ContractAPI contractAPI = retrofit.create(ContractAPI.class);
            Call<ContractItem> responseBodyCall = contractAPI.findContractByID(contractID);
            responseBodyCall.enqueue(new Callback<ContractItem>() {
                @Override
                public void onResponse(Call<ContractItem> call, Response<ContractItem> response) {
                    if (response.code() == 200) {
                        ContractItem obj = response.body();
                        String contractStatus = obj.getContractStatus();
                        if (contractStatus.equals(ImmutableValue.CONTRACT_INACTIVE)
                                || contractStatus.equals(ImmutableValue.CONTRACT_ACTIVE)
                                || contractStatus.equals(ImmutableValue.CONTRACT_FINISHED)) {
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
            //MainActivity.this.finish();
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
        }
    }

    private void searchAdvancedAction(){
        //Validate vehicle type
        if (spn_vehicleType.getSelectedItemPosition() == 0){
            vehicleType = ImmutableValue.XE_MAY;
        } else if (spn_vehicleType.getSelectedItemPosition() == 1){
            vehicleType = ImmutableValue.XE_CA_NHAN;
        } else {
            vehicleType = ImmutableValue.XE_DU_LICH;
        }

        //Validate seat
        if (spn_seat.getSelectedItemPosition() == 0){
            listSeat = new ArrayList<>();
            listSeat.add(2);
        } else if (spn_seat.getSelectedItemPosition() == 1){
            listSeat = new ArrayList<>();
            for (int i = 4;i <=7;i++){
                listSeat.add(i);
            }
        } else if (spn_seat.getSelectedItemPosition() == 2){
            listSeat = new ArrayList<>();
            for (int i = 8;i <=16;i++){
                listSeat.add(i);
            }
        } else if (spn_seat.getSelectedItemPosition() == 3){
            listSeat = new ArrayList<>();
            for (int i = 17;i <=32;i++){
                listSeat.add(i);
            }
        } else if (spn_seat.getSelectedItemPosition() == 4){
            listSeat = new ArrayList<>();
            for (int i = 33;i <=60;i++){
                listSeat.add(i);
            }
        }

        //Validate price type
        if (spn_priceType.getSelectedItemPosition() == 0){
            priceType = 1;
        } else {
            priceType = 3;
        }

        //Validate price
        if (fromPrice < 10000 || toPrice < 10000){
            Toast.makeText(this, "Giá tiền từ 10,000 trở lên", Toast.LENGTH_SHORT).show();
        } else {
            if (fromPrice > toPrice){
                Toast.makeText(this, "Giá tiền khoảng trước phải nhỏ hơn giá tiền khoảng sau", Toast.LENGTH_SHORT).show();
            } else {
                // Run successfull code
                ImmutableValue.listSearchAdvanced = new ArrayList<>();
                checkLoop = -1;
                ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Hệ thống",
                        "Đang xử lý", true);
                forwardLoop(dialog);

            }
        }

    }

    private void forwardLoop(final ProgressDialog dialog){
        if (checkLoop >= listSeat.size() - 1){
            dialog.dismiss();
            if (ImmutableValue.listSearchAdvanced.size() > 0){
                startActivity(new Intent(MainActivity.this, SearchAdvancedActivity.class));
            } else {
                Toast.makeText(this, "Không tìm thấy xe phù hợp", Toast.LENGTH_SHORT).show();
            }

            return;
        }
        checkLoop++;
        Retrofit retrofit = RetrofitConfig.getClient();
        VehicleAPI vehicleAPI = retrofit.create(VehicleAPI.class);
        Call<List<SearchVehicleItem>> responseBodyCall = vehicleAPI.getListAdvancedSearch(listSeat.get(checkLoop), vehicleType,
                fromPrice, toPrice, districtID, priceType);
        responseBodyCall.enqueue(new Callback<List<SearchVehicleItem>>() {
            @Override
            public void onResponse(Call<List<SearchVehicleItem>> call, Response<List<SearchVehicleItem>> response) {
                if (response.code() == 200){
                    ImmutableValue.listSearchAdvanced.addAll(response.body());
                }
                forwardLoop(dialog);
            }

            @Override
            public void onFailure(Call<List<SearchVehicleItem>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

}
