package com.example.manhvdse61952.vrc_android.remote;

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

public class ImmutableValue {

    public static String picturePath = "";
    public static File picture1 = null, picture2 = null, picture3 = null;
    ///////////////////// camera variable /////////////////////
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_OPEN_CODE = 0;

    public static final int CAMERA_VEHICLE_CODE_1 = 201;
    public static final int CAMERA_VEHICLE_CODE_2 = 202;
    public static final int CAMERA_VEHICLE_CODE_3 = 203;

    public static final int CAMERA_SELECT_IMAGE_CODE_1 = 1;
    public static final int CAMERA_SELECT_IMAGE_CODE_2 = 2;
    public static final int CAMERA_SELECT_IMAGE_CODE_3 = 3;

    public static final int NOTIFY_REQUEST_CODE = 401;

    /////////////////////////// main layout /////////////////

    public static final String SHARED_PREFERENCES_CODE = "VRS_GLOBAL_VALUE";
    public static final String IN_APP_SHARED_PREFERENCES_CODE = "VRS_LOCAL_VALUE";
    public static final String MAIN_SHARED_PREFERENCES_CODE = "VRS_MAIN_VALUE";

    /////////////////// GPS - location variable ////////////////
    LocationManager locationManager;
    public static double longtitudeCurrent = 0, latitudeCurrent = 0;
    public static String addressCurrent = "", cityCurrent = "", countryCurrent = "", districtCurrent = "";
    public static final int REQUEST_LOCATION = 50;
    ////////////////////////// Paypal ///////////////////
    public static final int PAYPAL_REQUEST_CODE = 301;


    ///////////////////////////////////// USE FOR CAMERA //////////////////////////////////////
    //check camera permission
    public void checkPermission(Context ctx, Activity atv, int imageCode) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && ctx.checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            atv.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            takePicture(ctx, atv, imageCode);
        }
    }

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
        File imgFile = new File(ImmutableValue.picturePath);
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

    //////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////// USE FOR GET CURRENT LOCATION //////////////////////////////////
    public void checkAddressPermission(final Context ctx, final Activity atv) {
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(ctx, atv);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation(ctx, atv);
        }
    }

    public void getLocation(Context ctx, Activity atv) {
        LocationManager locationManager = (LocationManager)ctx.getSystemService(Context.LOCATION_SERVICE);
        Location location;
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

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

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(atv, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ImmutableValue.REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                longtitudeCurrent = location.getLongitude();
                latitudeCurrent = location.getLatitude();
                getStringAddress(longtitudeCurrent, latitudeCurrent, ctx);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null){
                    //Toast.makeText(ctx, "Unable to get your location!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void buildAlertMessageNoGps(final Context ctx, final Activity atv) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
        builder.setMessage("Ứng dụng cần bật định vị để hoạt động").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ctx.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        getLocation(ctx, atv);
                    }
                })
                .setNegativeButton("Tắt ứng dụng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        atv.startActivity(intent);
                        dialog.cancel();
                    }
                });
        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public static void getStringAddress(double longitude, double latitude, Context ctx) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ctx, Locale.getDefault());

        try {
            addressCurrent = "";
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String district = addresses.get(0).getSubAdminArea();

            addressCurrent = address;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String removeAccentCharacter(String str) {
        str = str.toLowerCase().trim();
        str = str.replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a");
        str = str.replaceAll("đ", "d");
        str = str.replaceAll("[éèẻẽẹêếềểễệ]", "e");
        str = str.replaceAll("[íìỉĩị]", "i");
        str = str.replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o");
        str = str.replaceAll("[úùủũụưứừửữự]", "u");
        return str;
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

    public static String convertPrice(String defaultPrice){
        //String[] cutDoubleValue = defaultPrice.split(".");
        //String afterCutValue = cutDoubleValue[0];

        List<String> convertPriceList = new ArrayList<>();
        String reverse = new StringBuffer(defaultPrice).reverse().toString();
        String convertedPrice = "";
        if(reverse.length() > 3){
            for(int i=0;i <= reverse.length(); i+=3){
                convertPriceList.add(reverse.substring(i, Math.min(reverse.length(), i+3)));
            }
            convertedPrice = convertPriceList.get(0);
            for (int j = 1; j < convertPriceList.size(); j++){
                convertedPrice += "." + convertPriceList.get(j);
            }
            convertedPrice = new StringBuffer(convertedPrice).reverse().toString();
            String check = convertedPrice.substring(0,1);
            if (check.equals(".")){
                convertedPrice = convertedPrice.substring(1);
            }
        }
        else {
            convertedPrice = defaultPrice;
        }
        return convertedPrice;
    }

    public static String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("HH : mm, dd/MM/yyyy");
        return format.format(date);
    }

}
