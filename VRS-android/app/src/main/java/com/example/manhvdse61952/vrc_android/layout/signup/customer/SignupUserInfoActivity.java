package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.model.apiModel.City;
import com.example.manhvdse61952.vrc_android.model.apiModel.District;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.Validate;

import java.util.List;

public class SignupUserInfoActivity extends AppCompatActivity {

    int cityPosition = 0;
    Button btnBack, btnNext;
    ImageView imgPictureCMND, imgShowCMND, imgSelectPictureCMND;
    EditText edtSignupName, edtSignupPhone, edtSignupCNMD;
    TextInputLayout signup_name_txt, signup_phone_txt, signup_cmnd_txt;
    String name = "", phone = "", cmnd = "";
    Validate validObj;

    private ImmutableValue cameraObj = new ImmutableValue();
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_userinfo);


        //Declare id
        btnBack = (Button) findViewById(R.id.btnSignupAccountBack);
        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);
        imgPictureCMND = (ImageView) findViewById(R.id.imgPictureCMND);
        imgShowCMND = (ImageView) findViewById(R.id.imgSignupCMND);
        imgSelectPictureCMND = (ImageView) findViewById(R.id.imgSelectPictureCMND);
        edtSignupCNMD = (EditText) findViewById(R.id.edtSignupCMND);
        signup_name_txt = (TextInputLayout) findViewById(R.id.signup_name_txt);
        signup_phone_txt = (TextInputLayout) findViewById(R.id.signup_phone_txt);
        signup_cmnd_txt = (TextInputLayout) findViewById(R.id.signup_cmnd_txt);


        //button Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //galleryAddPic();

                //Declare id
                edtSignupName = (EditText) findViewById(R.id.edtSignupName);
                edtSignupPhone = (EditText) findViewById(R.id.edtSignupPhone);

                //Get value from edittex
                name = edtSignupName.getText().toString();
                phone = edtSignupPhone.getText().toString();
                cmnd = edtSignupCNMD.getText().toString();

                validObj = new Validate();
                Boolean checkName = validObj.validName(name, edtSignupName);
                Boolean checkPhone = validObj.validPhone(phone, edtSignupPhone);
                Boolean checkCMND = validObj.validCMND(cmnd, edtSignupCNMD);
                Boolean checkImage = validObj.validImageLink(ImmutableValue.picturePath, SignupUserInfoActivity.this);
                if (checkName && checkPhone && checkCMND && checkImage) {
                    dialog = ProgressDialog.show(SignupUserInfoActivity.this, "Đăng ký",
                            "Đang kiểm tra ...", true);
                    RetrofitCallAPI testApi = new RetrofitCallAPI();
                    testApi.checkExistedCmnd(cmnd, name, phone,
                            ImmutableValue.picturePath, SignupUserInfoActivity.this, edtSignupCNMD, dialog);
                }
            }
        });

        //button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2 = new Intent(SignupUserInfoActivity.this, SignupAccountActivity.class);
                startActivity(it2);
            }
        });


        //Button take picture
        imgPictureCMND.setOnClickListener(new View.OnClickListener() {
            //Use for android version 7 > with permission granted
            @Override
            public void onClick(View view) {
                cameraObj.checkPermission(SignupUserInfoActivity.this, SignupUserInfoActivity.this, ImmutableValue.CAMERA_OPEN_CODE);
            }
        });

        //button select image from gallery
        imgSelectPictureCMND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ImmutableValue.CAMERA_SELECT_IMAGE_CODE_1:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageGallery(data, imgShowCMND, SignupUserInfoActivity.this);
//                    Uri selectedImage = data.getData();
//                    imgShowCMND.setImageURI(selectedImage);
//                    try {
//
//                        //Create new file
//                        File f = new File(getApplicationContext().getCacheDir(), "example_image");
//                        f.createNewFile();
//
//                        //Convert bitmap to file
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                        byte[] bitmapData = bos.toByteArray();
//
//
//                        //write byte to file
//                        FileOutputStream fos = new FileOutputStream(f);
//                        fos.write(bitmapData);
//                        fos.flush();
//                        fos.close();
//                        File compressor = new Compressor(this).setQuality(75).compressToFile(f);
//                        pictureFilePath = compressor.getAbsolutePath();
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
                break;

            case ImmutableValue.CAMERA_OPEN_CODE:
                if (resultCode == RESULT_OK) {
                    cameraObj.showImageCamera(imgShowCMND, SignupUserInfoActivity.this);
                    //cameraObj.showImageCamera(imgShowCMND, SignupUserInfoActivity.this);


                    //Read text from image
//                    Bitmap bitmap = BitmapFactory.decodeFile(pictureFilePath);
//                    TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//                    if (!textRecognizer.isOperational()) {
//                        Toast.makeText(SignupUserInfoActivity.this, "Waiting ...", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//                        SparseArray<TextBlock> items = textRecognizer.detect(frame);
//                        if (items.size() != 0) {
//                            StringBuilder sb = new StringBuilder();
//                            for (int i = 0; i < items.size(); i++) {
//                                TextBlock item = items.valueAt(i);
//                                sb.append(item.getValue());
//                                sb.append("\n");
//                            }
//
//                            if (sb.toString() != null) {
//                                Toast.makeText(SignupUserInfoActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
//                                txtTestCMND.setText(sb.toString());
//                            }
//
//                        }
//
//                    }
                }
        }


    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(SignupUserInfoActivity.this, SignupAccountActivity.class);
        startActivity(it);
    }

    //Use for accept camera in phone when permission granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ImmutableValue.CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraObj.takePicture(SignupUserInfoActivity.this, SignupUserInfoActivity.this, ImmutableValue.CAMERA_OPEN_CODE);
                } else {
                    Toast.makeText(this, "Permission not granted !", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }
}
