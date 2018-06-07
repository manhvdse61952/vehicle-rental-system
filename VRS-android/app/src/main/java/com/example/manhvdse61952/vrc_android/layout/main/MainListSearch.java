package com.example.manhvdse61952.vrc_android.layout.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.SearchItem;

import java.util.ArrayList;
import java.util.List;

public class MainListSearch extends AppCompatActivity {

    ListView lvSearch;
    MainListSearchAdapter adapter;
    List<SearchItem> listSearchDemo = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list_search);

        //Add demo some search list result

        SearchItem obj = new SearchItem();
        obj.setImg_vehicle("R.drawable.xe_oto");
        obj.setVehicle_name("Toyota Fortuner");
        obj.setSeat(4);
        obj.setRent_price("400.000");
        obj.setVehicle_description("Xe mới mua, nội thất hiện đại. Xe phù hợp với mọi loại địa hình, tiết kiệm nhiên liệu");
        listSearchDemo.add(obj);

        SearchItem obj2 = new SearchItem();
        obj2.setImg_vehicle("R.drawable.img_owner");
        obj2.setVehicle_name("Huyndai ABC");
        obj2.setSeat(7);
        obj2.setRent_price("600.000");
        obj2.setVehicle_description("Xe phù hợp khi đi du lịch với gia đình, cốp sau rộng rãi, xe còn mới");
        listSearchDemo.add(obj2);

        SearchItem obj3 = new SearchItem();
        obj3.setImg_vehicle("R.drawable.img_owner");
        obj3.setVehicle_name("Mitsubishi");
        obj3.setSeat(4);
        obj3.setRent_price("300.000");
        obj3.setVehicle_description("Xe phù hợp khi đi du lịch với gia đình, cốp sau rộng rãi, xe còn mới");
        listSearchDemo.add(obj3);

        SearchItem obj4 = new SearchItem();
        obj4.setImg_vehicle("R.drawable.img_owner");
        obj4.setVehicle_name("Honda civic");
        obj4.setSeat(7);
        obj4.setRent_price("500.000");
        obj4.setVehicle_description("Xe phù hợp khi đi du lịch với gia đình, cốp sau rộng rãi, xe còn mới");
        listSearchDemo.add(obj4);


        lvSearch = (ListView)findViewById(R.id.lvSearch);
        adapter = new MainListSearchAdapter(this, listSearchDemo, getApplicationContext());
        lvSearch.setAdapter(adapter);

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(MainListSearch.this, MainItem.class);
                startActivity(it);
            }
        });
    }
}
