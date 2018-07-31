package com.example.manhvdse61952.vrc_android.controller.layout.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.layout.vehicle.showdetail.VehicleDetail;
import com.example.manhvdse61952.vrc_android.controller.resources.GeneralController;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_model.Tracking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastLocation = null;
    Polyline polylineFinal;
    int circleDistance = 10000; //~ 10 km

    LinearLayout ln_detail;
    TextView txt_vehicle_name, txt_distance, txt_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //Init map system
        declareID();

        GeneralController.scaleView(ln_detail, 0);

    }

    private void declareID(){
        ln_detail = (LinearLayout)findViewById(R.id.ln_detail);
        txt_vehicle_name = (TextView)findViewById(R.id.txt_vehicle_name);
        txt_distance = (TextView)findViewById(R.id.txt_distance);
        txt_duration = (TextView)findViewById(R.id.txt_duration);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (lastLocation == null) {
            getCurrentLocation();

        }


    }

    private void getCurrentLocation() {
        final ProgressDialog dialog = ProgressDialog.show(MapsActivity.this, "Hệ thống",
                "Đang xử lý", true);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                locationManager.removeUpdates(locationListener);
//                lastLocation = location;
//                addMakerAndCircle();
//                dialog.dismiss();
//                getLocationFromDB();
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
            dialog.dismiss();
            Toast.makeText(this, "Vui lòng thoát ứng dụng và chạy lại", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            addMakerAndCircle();
            dialog.dismiss();
            getLocationFromDB();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            addMakerAndCircle();
            dialog.dismiss();
            getLocationFromDB();
        }
    }

    private void addMakerAndCircle() {
        GoogleMap googleMap = null;
        if (mMap == null) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(false);
            mMap.setIndoorEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        double lat = lastLocation.getLatitude();
        double lng = lastLocation.getLongitude();
        LatLng lastLatLng = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(lastLatLng)
                .title("Vị trí của bạn")
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 13.0f));
        mMap.addCircle(new CircleOptions().center(lastLatLng).radius(circleDistance).
                strokeColor(Color.argb(10, 0, 0, 255)).
                fillColor(Color.argb(10, 0, 0, 255)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed() {
        MapsActivity.this.finish();
        super.onBackPressed();
    }

    //Execute input value
    private void getRouteToMarker(LatLng currentPos, LatLng vehiclePos) {
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(currentPos, vehiclePos);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

    }

    //Get direction by google maps API
    public String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = new ArrayList<LatLng>();
            PolylineOptions lineOptions = new PolylineOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }

            if (points.size() != 0) {
                // Drawing polyline in the Google Map for the i-th route
                polylineFinal = mMap.addPolyline(lineOptions);
                if (ImmutableValue.mapsDetail.size() > 0){
                    txt_duration.setText(ImmutableValue.mapsDetail.get(0));
                    txt_distance.setText(ImmutableValue.mapsDetail.get(1));
                }
            }
        }
    }

    //Get all location on DB
    private void getLocationFromDB() {
        final List<Tracking> listTracking = new ArrayList<>();
        final List<Tracking> listFinalTracking = new ArrayList<>();

        //Test value
        listTracking.add(new Tracking("DLMRR8745R9845476", 106.629545, 10.853224));
        listTracking.add(new Tracking("HFEFE1657U4856411", 106.658632, 10.836006));
        listTracking.add(new Tracking("KJ113564", 106.634225,10.806666));
        listTracking.add(new Tracking("KBBFW5210E5454861", 106.800352,10.865851));
        double lat = lastLocation.getLatitude();
        double lng = lastLocation.getLongitude();
        final LatLng lastLatLng = new LatLng(lat, lng);
        //Add all vehicle in circle distance
        if (listTracking.size() != 0) {
            for (int i = 0; i < listTracking.size(); i++) {
                Location vehicleLocation = new Location("");
                double vehicleLat = listTracking.get(i).getLatitude();
                double vehicleLng = listTracking.get(i).getLongitude();
                vehicleLocation.setLatitude(vehicleLat);
                vehicleLocation.setLongitude(vehicleLng);
                if (lastLocation.distanceTo(vehicleLocation) <= circleDistance){
                    listFinalTracking.add(listTracking.get(i));
                }
            }
        }

        //show maker in maps
        if (listFinalTracking.size() > 0){
            for (int i = 0; i < listFinalTracking.size();i++){
                double vehicleLat = listFinalTracking.get(i).getLatitude();
                double vehicleLng = listFinalTracking.get(i).getLongitude();
                LatLng vehicleLatLng = new LatLng(vehicleLat, vehicleLng);
                mMap.addMarker(new MarkerOptions().position(vehicleLatLng)
                        .title(listTracking.get(i).getVehicleFrameNumber())
                        .snippet(listTracking.get(i).getVehicleFrameNumber())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                );
            }
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    GeneralController.scaleView(ln_detail, 0);
                    for (int i = 0; i < listFinalTracking.size(); i++){
                        if (marker.getTitle().equals(listFinalTracking.get(i).getVehicleFrameNumber())){
                            double vehicleLat = listFinalTracking.get(i).getLatitude();
                            double vehicleLng = listFinalTracking.get(i).getLongitude();
                            LatLng vehicleLatLng = new LatLng(vehicleLat, vehicleLng);
                            if (polylineFinal != null){
                                polylineFinal.remove();
                                getRouteToMarker(lastLatLng, vehicleLatLng);
                            } else {
                                getRouteToMarker(lastLatLng, vehicleLatLng);
                            }
                            (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    GeneralController.scaleView(ln_detail, 170);
                                }
                            }, 500);
                            txt_vehicle_name.setText(marker.getTitle());
                        }
                    }

                    return true;
                }
            });
        }

    }
}

