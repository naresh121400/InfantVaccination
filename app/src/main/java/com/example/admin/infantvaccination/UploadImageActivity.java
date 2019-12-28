package com.example.admin.infantvaccination;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadImageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ImageView imgChild;
    Button btnUpload;
    Bitmap imgBitMap;
    String cid;
    public final int PERMISSION_REQUEST_CODE = 654;
    private int REQUEST_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);


        //ToolBar

        toolbar = findViewById(R.id.uploadImageTB);
        toolbar.setTitle("Upload Iamge");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);


        imgChild=findViewById(R.id.imgUploadAct);
        btnUpload=findViewById(R.id.btnUploadImgAct);
        btnUpload.setEnabled(false);
        cid=getIntent().getStringExtra("cid").toString();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UploadImageActivity.this, MainActivity.class);
                intent.putExtra("uid",MainActivity.uid.toString());
                intent.putExtra("type",MainActivity.type.toString());
                startActivity(intent);
                finish();
            }
        });


        imgChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CharSequence colors[] = new CharSequence[] {"Take photo from camera", "Take photo from gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(UploadImageActivity.this);
                builder.setTitle("Options");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==1){

                            if(Build.VERSION.SDK_INT >=23) {
                                if (checkPermission()){
                                    pickImage();

                                }else{
                                    requestPermission();
                                }
                            }
                            else{
                                pickImage();
                            }

                        }
                        else if(which==0){
                            cameraIntent();
                        }
                    }
                });
                builder.show();




            }
        });



        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });






    }

    private void uploadImage() {

        DbBitmapUtility dbm=new DbBitmapUtility();
        byte[] image=dbm.getBytes(imgBitMap);
        AddChild ac=new AddChild();
        ac.setId(cid);
        ac.setImage(image);
        int res=MainActivity.sqLiteHelper.changeImage(ac);

        if(res>0){

            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            Intent it=new Intent(UploadImageActivity.this,MainActivity.class);
            it.putExtra("uid",MainActivity.uid.toString());
            it.putExtra("type",MainActivity.type.toString());
            startActivity(it);
            finish();
        }



    }

    private void pickImage() {


        Intent in = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        in.putExtra("crop", "true");
        in.putExtra("outputX", 100);
        in.putExtra("outputY", 100);
        in.putExtra("scale", true);
        in.putExtra("return-data", true);

        startActivityForResult(in, 1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            imgChild.setImageBitmap(bmp);
            imgBitMap=bmp;
            btnUpload.setEnabled(true);


        }
        else if(requestCode==REQUEST_CAMERA && resultCode==RESULT_OK && data !=null){

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgChild.setImageBitmap(thumbnail);
            imgBitMap=thumbnail;
            btnUpload.setEnabled(true);

        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Accepted", Toast.LENGTH_SHORT).show();


                pickImage();



            } else {

            }
        }

    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(UploadImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE );
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadImageActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(UploadImageActivity.this, "Write External Sto.rage permission allows us to access images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(UploadImageActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UploadImageActivity.this, MainActivity.class);
        intent.putExtra("uid",MainActivity.uid.toString());
        intent.putExtra("type",MainActivity.uid.toString());
        startActivity(intent);
        finish();
    }
    private void cameraIntent()
    {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(in, REQUEST_CAMERA);
    }

}
