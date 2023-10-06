package com.example.stylist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import android.widget.ImageView;



public class OutfitRecommend extends AppCompatActivity {

    private Button camera;
    int imageSize = 224;
    private ImageView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_recommend);

        //권한 체크

        result =findViewById(R.id.result);
        camera = findViewById(R.id.camera);


        };

    }

