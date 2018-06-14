package com.example.manhvdse61952.vrc_android.remote;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class ImmutableValue {
    public static final String MESSAGE_CODE = "VRS10";

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
    public static String picturePath = "";

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
}
