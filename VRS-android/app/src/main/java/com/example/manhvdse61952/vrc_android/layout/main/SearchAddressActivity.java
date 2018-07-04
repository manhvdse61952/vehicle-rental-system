package com.example.manhvdse61952.vrc_android.layout.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class SearchAddressActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("VRSPlace", "Place: " + place.getName().toString());
                Toast.makeText(SearchAddressActivity.this, place.getName().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Status status) {
                Log.i("VRSPlace", "An error occurred: " + status);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
