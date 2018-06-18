package com.example.manhvdse61952.vrc_android.remote;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupUserInfoActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.SignupOwnerThree;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.SignupOwnerTwoPlus;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class ImmutableValue {
    public static final String MESSAGE_CODE = "VRS10";

    public static String picturePath = "";
    ///////////////////// camera variable /////////////////////
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_OPEN_CODE = 0;

    public static final int CAMERA_VEHICLE_CODE_1 = 201;
    public static final int CAMERA_VEHICLE_CODE_2 = 202;
    public static final int CAMERA_VEHICLE_CODE_3 = 203;
    public static final int CAMERA_VEHICLE_CODE_4 = 204;
    public static final int CAMERA_VEHICLE_CODE_5 = 205;
    public static final int CAMERA_VEHICLE_CODE_6 = 206;



    public static final int CAMERA_SELECT_IMAGE_CODE = 1;
    public static final int CAMERA_SELECT_IMAGE_CODE_2 = 2;
    public static final String SHARED_PREFERENCES_CODE = "VRS_GLOBAL_VALUE";
    ////////////////////////////////////////////////////////////

    /////////////////// GPS - location variable ////////////////
    LocationManager locationManager;
    public static double longtitudeCurrent = 0, latitudeCurrent = 0;
    public static String address = "", city = "", country = "", district = "", knownName = "";
    public static final int REQUEST_LOCATION = 50;
    ////////////////////////////////////////////////////////////

    ///////////////////////////////////// USE FOR CAMERA //////////////////////////////////////
    //check camera permission
    public void checkPermission(Context ctx, Activity atv, int imageCode){
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
    public void showImageGallery(Intent data, ImageView imgShow, Context ctx) {
        Uri selectedImage = data.getData();
        imgShow.setImageURI(selectedImage);
        try {

            //Create new file
            File f = new File(ctx.getCacheDir(), "example_image");
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
    }

    //Show picture in camera
    public void showImageCamera(ImageView imgShow, Context ctx) {
        File imgFile = new File(ImmutableValue.picturePath);
        if (imgFile.exists()) {
            Picasso.get().load(imgFile).into(imgShow);
            File compressor = null;
            try {
                compressor = new Compressor(ctx).setQuality(75).compressToFile(imgFile);
                picturePath = compressor.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////// USE FOR GET CURRENT LOCATION //////////////////////////////////
    public void checkAddressPermission(final Context ctx, final Activity atv){
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(ctx, atv);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation(ctx, atv);
        }
    }

    public void getLocation(Context ctx, Activity atv) {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(atv, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ImmutableValue.REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                longtitudeCurrent = location.getLongitude();
                latitudeCurrent = location.getLatitude();
                getStringAddress(longtitudeCurrent, latitudeCurrent, ctx);
            } else {
                Toast.makeText(ctx, "Unable to get your location!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void buildAlertMessageNoGps(final Context ctx, final Activity atv) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ctx);
        builder.setMessage("Ứng dụng cần bật định vị để hoạt động").setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getLocation(ctx, atv);
                        ctx.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    public void getStringAddress(double longitude, double latitude, Context ctx){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ctx, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String state = addresses.get(0).getLocality();
            city = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            district = addresses.get(0).getSubAdminArea();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
