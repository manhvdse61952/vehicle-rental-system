package com.example.manhvdse61952.vrc_android.controller.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class PermissionDevice {

    public static String picturePath = "";
    //Camera code /////////////////////
    public static final int CAMERA_REQUEST_CODE = 101;

    public static final int CAMERA_OPEN_CODE = 0;
    public static final int CAMERA_VEHICLE_CODE_1 = 201;
    public static final int CAMERA_VEHICLE_CODE_2 = 202;
    public static final int CAMERA_VEHICLE_CODE_3 = 203;

    public static final int CAMERA_SELECT_IMAGE_CODE_1 = 1;
    public static final int CAMERA_SELECT_IMAGE_CODE_2 = 2;
    public static final int CAMERA_SELECT_IMAGE_CODE_3 = 3;

    //Notify code
    public static final int NOTIFY_REQUEST_CODE = 401;

    //GPS - location code ////////////////
    LocationManager locationManager;
    public static final int REQUEST_LOCATION = 50;
    public static String currentLocation = "";

    // Paypal ///////////////////
    public static final int PAYPAL_REQUEST_CODE = 301;

    ///////////////////////////////////// USE FOR CAMERA //////////////////////////////////////
    //Open camera
    public void takePicture(Context ctx, Activity atv, int code) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(ctx.getPackageManager()) != null) {
            File pictureFile = null;
            pictureFile = getPictureFile(ctx);
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ctx,
                        "com.example.manhvdse61952.vrc_android.provider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                atv.startActivityForResult(cameraIntent, code);
            }
        }
    }

    //Generate file in local
    public File getPictureFile(Context ctx) {
        picturePath = "";
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String pictureFile = "IMG_" + timeStamp;
            File storageDir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(pictureFile, ".jpg", storageDir);
            picturePath = image.getAbsolutePath();
            return image;
        } catch (Exception ex) {
            Log.e("getPictureFile", ex.getMessage());
            Toast.makeText(ctx, "ERROR: at getPictureFile" + ex.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    //Show picture in gallery
    public String showImageGallery(Intent data, ImageView imgShow, Context ctx) {
        Uri selectedImage = data.getData();
        imgShow.setImageURI(selectedImage);
        try {

            //Create new file
            File f = new File(ctx.getCacheDir(), String.valueOf(new Date().getTime()));
            f.createNewFile();

            //Convert bitmap to file
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), selectedImage);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapData = bos.toByteArray();


            //write byte to file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            File compressor = new Compressor(ctx).setQuality(75).compressToFile(f);
            picturePath = compressor.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return picturePath;
    }

    //Show picture in camera
    public String showImageCamera(ImageView imgShow, Context ctx) {
        File imgFile = new File(PermissionDevice.picturePath);
            if (imgFile.exists()) {
            File compressor = null;
            try {
                compressor = new Compressor(ctx).setQuality(75).compressToFile(imgFile);
                Picasso.get().load(compressor).into(imgShow);
                picturePath = compressor.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return picturePath;
    }

    /////////////////////////// USE FOR GET CURRENT LOCATION //////////////////////////////////
    public static String getLocation(final Context ctx, Activity atv) {
        currentLocation = "";
        LocationManager locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
        Location location;
        if (locationManager != null){
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double longtitudeCurrent = location.getLongitude();
                    double latitudeCurrent = location.getLatitude();
                    currentLocation = getStringAddress(longtitudeCurrent, latitudeCurrent, ctx);
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

            if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(atv, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PermissionDevice.REQUEST_LOCATION);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    double longitudeCurrent = location.getLongitude();
                    double latitudeCurrent = location.getLatitude();
                    currentLocation = getStringAddress(longitudeCurrent, latitudeCurrent, ctx);
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location == null){
                        Toast.makeText(ctx, "Unable to get your location!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        return currentLocation;
    }

    public static String getStringAddress(double longitude, double latitude, Context ctx) {
        String currentAddress = "";
        String addressFull = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ctx, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            currentAddress = addresses.get(0).getAddressLine(0);
//            String city = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String district = addresses.get(0).getSubAdminArea();
            if (!currentAddress.trim().equals("")){
                String[] arrayAddressTemp = currentAddress.split(",");
                addressFull = arrayAddressTemp[0];
                for (int i = 1; i < arrayAddressTemp.length - 1;i++){
                    addressFull = addressFull + ", " + arrayAddressTemp[i].trim();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressFull;
    }

    public static String getStringDistrict(double longitude, double latitude, Context ctx) {
        String currentDistrict = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ctx, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            String city = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
            currentDistrict = addresses.get(0).getSubAdminArea();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentDistrict;
    }

    public String getCmndFromImage(String filename, Context ctx) {
        //Temp variable
        String getCmndFromImage = "";

        //Convert image are displayed in image-view to bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(filename);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(ctx.getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(ctx, "Vui lòng thử lại ...", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            if (items.size() != 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    TextBlock item = items.valueAt(i);
                    sb.append(item.getValue());
                    sb.append("\n");
                }

                if (sb.toString() != null) {
                    String strFromOcrReader = sb.toString().trim().replaceAll("\\s+", "");
                    String strTemp = "";
                    String[] arrayCmndTemp = strFromOcrReader.split("");
                    //get each character in OCR reader and check it's a digit or not
                    //Error strinArrayIndexOutOfBound when we set i = 0, so that we set i = 1 to ignore this error
                    for (int i = 1; i < arrayCmndTemp.length; i++) {
                        if (Character.isDigit(arrayCmndTemp[i].charAt(0))) {
                            strTemp += arrayCmndTemp[i].charAt(0);
                        } else {
                            strTemp += "-";
                        }
                    }

                    //execute string temp to get CMND
                    String[] arrayResult = strTemp.split("-");
                    for (int i = 0; i < arrayResult.length; i++) {
                        if (arrayResult[i].length() >= 7) {
                            getCmndFromImage = arrayResult[i];
                            break;
                        }
                    }
                }

            }

        }

        return getCmndFromImage;
    }

}
