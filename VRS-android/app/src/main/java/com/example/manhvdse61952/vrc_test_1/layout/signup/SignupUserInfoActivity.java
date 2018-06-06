package com.example.manhvdse61952.vrc_test_1.layout.signup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_test_1.R;
import com.example.manhvdse61952.vrc_test_1.model.SignupObj;
import com.example.manhvdse61952.vrc_test_1.remote.ImmutableValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupUserInfoActivity extends AppCompatActivity {

    TextView txtTestCMND;
    Button btnBack, btnNext;
    ImageView imgPictureCMND, imgSignupCMND, imgSelectPictureCMND;
    String receiveValue = "";
    SignupObj signupObj = new SignupObj();

    EditText edtSignupName, edtSignupPhone, edtSignupCNMD, edtSignupPaypal;
    Spinner spnAddress;
    String name = "", phone = "", cmnd = "", paypal = "", address = "";

    //New test//
    private String pictureFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_userinfo);

        Intent receiveIt = getIntent();
        receiveValue = receiveIt.getStringExtra(ImmutableValue.MESSAGE_CODE);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            signupObj = objectMapper.readValue(receiveValue, SignupObj.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnBack = (Button) findViewById(R.id.btnSignupAccountBack);
        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);
        imgPictureCMND = (ImageView) findViewById(R.id.imgPictureCMND);
        imgSignupCMND = (ImageView) findViewById(R.id.imgSignupCMND);
        imgSelectPictureCMND = (ImageView) findViewById(R.id.imgSelectPictureCMND);
        edtSignupCNMD = (EditText) findViewById(R.id.edtSignupCMND);
        txtTestCMND = (TextView)findViewById(R.id.txtTest);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryAddPic();

                edtSignupName = (EditText) findViewById(R.id.edtSignupName);
                edtSignupPhone = (EditText) findViewById(R.id.edtSignupPhone);
                edtSignupPaypal = (EditText) findViewById(R.id.edtSignupPaypal);
                spnAddress = (Spinner) findViewById(R.id.spnAddress);

                name = edtSignupName.getText().toString();
                phone = edtSignupPhone.getText().toString();
                cmnd = edtSignupCNMD.getText().toString();
                paypal = edtSignupPaypal.getText().toString();
                address = spnAddress.getSelectedItem().toString();

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    Intent it = new Intent(SignupUserInfoActivity.this, SignupRoleActivity.class);
                    signupObj.setName(name);
                    signupObj.setPhone(phone);
                    signupObj.setIdCard(cmnd);
                    signupObj.setPaypalID(paypal);
                    signupObj.setAddress(address);
                    String json = objectMapper.writeValueAsString(signupObj);
                    it.putExtra(ImmutableValue.MESSAGE_CODE, json);
                    Toast.makeText(SignupUserInfoActivity.this, name, Toast.LENGTH_SHORT).show();
                    startActivity(it);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent it2 = new Intent(SignupUserInfoActivity.this, SignupAccountActivity.class);
//                startActivity(it2);
                Bitmap thumbnail = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.text);
                imgSignupCMND.setImageBitmap(thumbnail);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), R.drawable.text, options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                if (imageHeight > imageWidth) {
                    imgSignupCMND.setRotation(-90);
                }


                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    Toast.makeText(SignupUserInfoActivity.this, "Waiting ...", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(thumbnail).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    if (items.size() != 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < items.size(); i++) {
                            TextBlock item = items.valueAt(i);
                            sb.append(item.getValue());
                            sb.append("\n");
                        }

                        if (sb.toString() != null) {
                            Toast.makeText(SignupUserInfoActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                            txtTestCMND.setText(sb.toString());
                        }

                    }

                }
            }
        });

        ///////////////// Only use for test with class SignupUserInfoCameraActivity
//        imgPictureCMND.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED){
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
//                } else{
//                    Intent it = new Intent(SignupUserInfoActivity.this, SignupUserInfoCameraActivity.class);
//                    startActivity(it);
//                    finish();
//                }
//            }
//        });
        //////////////////////////////////////////////////////////////////////////////


        //Button take picture
        imgPictureCMND.setOnClickListener(new View.OnClickListener() {
            //Use for android version 7 > with permission granted
            @Override
            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, ImmutableValue.CAMERA_REQUEST_CODE);
//                } else {
//                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(it, 0);
//                }


                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, ImmutableValue.CAMERA_REQUEST_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        File pictureFile = null;
                        try {
                            pictureFile = getPictureFile();
                        } catch (IOException ex) {
                            Toast.makeText(SignupUserInfoActivity.this,
                                    "Photo file can't be created, please try again",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (pictureFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(SignupUserInfoActivity.this,
                                    "com.example.manhvdse61952.vrc_test_1.provider",
                                    pictureFile);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(cameraIntent, ImmutableValue.CAMERA_OPEN_CODE);
                        }

                    }
                }
            }
        });

        //button select image from gallery
        imgSelectPictureCMND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE);
            }
        });

    }

    //Get image file name
    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "IMG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    //Save image to gallery when click next button
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureFilePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    imgSignupCMND.setImageURI(selectedImage);
                }
                break;
            case ImmutableValue.CAMERA_OPEN_CODE:
                if (resultCode == RESULT_OK) {
                    File imgFile = new File(pictureFilePath);
                    if (imgFile.exists()) {
                        imgSignupCMND.setImageURI(Uri.fromFile(imgFile));
                    }

                    //Read text from image
                    Bitmap bitmap = BitmapFactory.decodeFile(pictureFilePath);

//                    Uri imageUri = data.getData();
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                        if (!textRecognizer.isOperational()) {
                            Toast.makeText(SignupUserInfoActivity.this, "Waiting ...", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(SignupUserInfoActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                                    txtTestCMND.setText(sb.toString());
                                }

                            }

                        }




//                    if (data != null && data.getExtras() != null) {
//                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                        imgSignupCMND.setImageBitmap(thumbnail);
//                        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//                        if (!textRecognizer.isOperational()) {
//                            Toast.makeText(this, "Waiting ...", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Frame frame = new Frame.Builder().setBitmap(thumbnail).build();
//                            SparseArray<TextBlock> items = textRecognizer.detect(frame);
//                            if (items.size() != 0) {
//                                StringBuilder sb = new StringBuilder();
//                                for (int i = 0; i < items.size(); i++) {
//                                    TextBlock item = items.valueAt(i);
//                                    sb.append(item.getValue());
//                                    sb.append("\n");
//                                }
//                                if (sb != null) {
//                                    txtTestCMND.setText(sb.toString());
//                                }
//
//                            }
//
//                        }
//                    }
                }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupUserInfoActivity.this, SignupAccountActivity.class);
        startActivity(it);
    }

    //Use for accept camera in phone when permission granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ImmutableValue.CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(it, 0);
                } else {
                    Toast.makeText(this, "Permission not granted !", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }
}
