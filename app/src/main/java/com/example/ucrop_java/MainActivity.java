package com.example.ucrop_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView img;
    private final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_EXTERNAL_STORAGE};
        if( ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]) == PackageManager.PERMISSION_DENIED
                || ActivityCompat.checkSelfPermission(MainActivity.this, permissions[1]) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 100);
        }
        
        init();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK)
                .setType("image/*"),CODE_IMG_GALLERY);
            }
        });
    }

    private void init() {
        this.img = findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CODE_IMG_GALLERY && resultCode==RESULT_OK){
            Uri imageUri = data.getData();
            if(imageUri !=null){
                startCrop(imageUri);
            }
        }else if(requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            Uri imageResultCrop = UCrop.getOutput(data);
            if(imageResultCrop!=null){
                img.setImageURI(imageResultCrop);
            }
        }
    }

    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName +=".jpg";
        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));

        uCrop.withAspectRatio(1,1);

        //아래처럼하고 저 아래의 setFreeStyleCropEnabled을 false로 하면 고정영역됨
//        uCrop.withAspectRatio(16,9);

        uCrop.withMaxResultSize(450,450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(MainActivity.this);
    }

    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);

        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        options.setStatusBarColor(getResources().getColor(R.color.design_default_color_primary_dark));
        options.setToolbarColor(getResources().getColor(R.color.design_default_color_primary));
        options.setToolbarTitle("hihi");

        return options;
    }

}