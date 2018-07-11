package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.contract.ManageContractActivity;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.layout.vehicle.ManageVehicleActivity;
import com.example.manhvdse61952.vrc_android.layout.vehicle.MotobikeTab;
import com.example.manhvdse61952.vrc_android.layout.vehicle.CarTab;
import com.example.manhvdse61952.vrc_android.layout.vehicle.TravelCarTab;
import com.example.manhvdse61952.vrc_android.model.apiModel.City;
import com.example.manhvdse61952.vrc_android.model.apiModel.District;
import com.example.manhvdse61952.vrc_android.model.searchModel.SearchVehicleItem;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView img_current_address, main_extra_search;
    TextView txt_main_search_place, txt_main_search_address;
    ImmutableValue locationObj = new ImmutableValue();
    //TextView txt_main_search_address;

    ///////////////// USE FOR SEARCH ADAPTER ////////
    public static List<SearchVehicleItem> listMotorbike = new ArrayList<>();
    public static List<SearchVehicleItem> listPersonalCar = new ArrayList<>();
    public static List<SearchVehicleItem> listTravelCar = new ArrayList<>();
    public static List<District> listAllDistrict = new ArrayList<>();
    //////////////////////////////////////////////////////////////////////

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

        img_current_address = (ImageView)findViewById(R.id.img_current_address);
        txt_main_search_place = (TextView)findViewById(R.id.txt_main_search_place);
        txt_main_search_address = (TextView)findViewById(R.id.txt_main_search_address);



        //Place Autocomplete
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                ImmutableValue.getStringAddress(place.getLatLng().longitude, place.getLatLng().latitude, MainActivity.this);
                txt_main_search_place.setText(place.getName());
                String addressFull = "";
                if (!ImmutableValue.addressCurrent.trim().equals("")){
                    String[] arrayAddressTemp = ImmutableValue.addressCurrent.split(",");
                    addressFull = arrayAddressTemp[0];
                    for (int i = 1; i < arrayAddressTemp.length - 1;i++){
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

        //Get vehicle by district
        getAllDistrict();
        locationObj.checkAddressPermission(MainActivity.this, MainActivity.this);
        txt_main_search_address.setText(ImmutableValue.addressCurrent + "");

        getAllVehicleByDistrictID(44);
//        int districtID = getDistrictIdByName("quận bình thạnh");
//        if (districtID == 0) {
//            getAllVehicleByDistrictID(44);
//        } else {
//            getAllVehicleByDistrictID(districtID);
//        }

        img_current_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationObj.checkAddressPermission(MainActivity.this, MainActivity.this);
                String addressFull = ImmutableValue.addressCurrent.replaceAll("Vietnam", "");
                txt_main_search_address.setText(addressFull);
                txt_main_search_place.setText("Vị trí hiện tại");
            }
        });

        main_extra_search = (ImageView) findViewById(R.id.main_extra_search);
        main_extra_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent it = new Intent(MainActivity.this, MapsActivity.class);
//                startActivity(it);

                /////////////////// USE FOR TEST SEARCH //////////////////////
//                new SimpleSearchDialogCompat(MainActivity.this, "", "Nhập đia điểm cần kiếm xe", null, initData(), new SearchResultListener<Searchable>() {
//                    @Override
//                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
//                        Toast.makeText(MainActivity.this, "" + searchable.getTitle(), Toast.LENGTH_SHORT).show();
//                        if (!searchable.getTitle().equals("")) {
//
//                        }
//                        baseSearchDialogCompat.dismiss();
//                    }
//                }).show();
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
        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        View hView = navigationView.getHeaderView(0);
        TextView txtNavFullname = (TextView) hView.findViewById(R.id.txtNavFullname);
        TextView txtNavUsername = (TextView) hView.findViewById(R.id.txtNavUsername);
        TextView txtNavRole = (TextView) hView.findViewById(R.id.txtNavRole);
        txtNavUsername.setText(editor.getString("usernameAfterLogin", "Empty"));
        txtNavFullname.setText(editor.getString("fullName", "Empty"));
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
        secAdapter.addFragment(new MotobikeTab(), "Xe máy");
        secAdapter.addFragment(new CarTab(), "Ô tô cá nhân");
        secAdapter.addFragment(new TravelCarTab(), "Ô tô du lịch");
        viewPager.setAdapter(secAdapter);
    }


    ///////////////////////////////////////////////////////////////


    //////////////////////////////////// USE FOR SEARCH //////////////////////////////////
//    private ArrayList<SearchAddressModel> initData() {
//        ArrayList<SearchAddressModel> items = new ArrayList<>();
//        for (int i = 0; i < listAllDistrict.size(); i++) {
//            items.add(new SearchAddressModel(listAllDistrict.get(i).getDistrictName()));
//        }
//        return items;
//    }
    //////////////////////////////////////////////////////////////////////////////////////

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
//        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất ?").setCancelable(false)
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences settings = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            settings.edit().clear().commit();
                            SharedPreferences settings_2 = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            settings_2.edit().clear().commit();
                            SharedPreferences settings_3 = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            settings_3.edit().clear().commit();
                            Intent it = new Intent(MainActivity.this, LoginActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private void getAllDistrict() {
        List<City> listCity = RetrofitCallAPI.lisCityTest;
        for (int i = 0; i < listCity.size(); i++) {
            List<District> listDistrict = listCity.get(i).getDistrict();
            for (int j = 0; j < listDistrict.size(); j++) {
                District districtObj = new District();
                districtObj.setId(listDistrict.get(j).getId());
                districtObj.setDistrictName(listDistrict.get(j).getDistrictName() + ", " + listCity.get(i).cityName);
                listAllDistrict.add(districtObj);
            }
        }
    }

    private int getDistrictIdByName(String districtName) {
        List<City> listCity = RetrofitCallAPI.lisCityTest;
        int districtID = 0;
        for (int i = 0; i < listCity.size(); i++) {
            List<District> listDistrict = listCity.get(i).getDistrict();
            for (int j = 0; j < listDistrict.size(); j++) {
                //String districtConvert = listDistrict.get(j).getDistrictName().toLowerCase().replaceAll("[quận|huyện]", "");
//                if (districtConvert.equals(districtName)){
//                    districtID = listDistrict.get(j).getId();
//                    break;
//                }

                if (listDistrict.get(j).getDistrictName().toLowerCase().equals(districtName)) {
                    districtID = listDistrict.get(j).getId();
                    break;
                }
            }
        }
        return districtID;
    }

    /// Get all vehicle by districtID ///
    public void getAllVehicleByDistrictID(int districtID) {
        Retrofit test = RetrofitConnect.getClient();
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
                            if (listAll.get(i).getVehicleType().equals("XE_MAY")) {
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
                            } else if (listAll.get(i).getVehicleType().equals("XE_CA_NHAN")) {
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
                            } else if (listAll.get(i).getVehicleType().equals("XE_DU_LICH")) {
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
                        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                        int tabIndex = editor.getInt("tabIndex", 0);
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

            }

            @Override
            public void onFailure(Call<List<SearchVehicleItem>> call, Throwable t) {
                viewPager = (ViewPager) findViewById(R.id.container);
                setupViewPager(viewPager);
                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager);
                createTabIcons();
                Toast.makeText(MainActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
