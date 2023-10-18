package com.example.stylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    private Button goToOutfit,goToColorMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToOutfit = findViewById(R.id.goToOutfit);
        goToColorMatch = findViewById(R.id.goToColorMatch);

        goToOutfit.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), OutfitRecommend.class);
            startActivity(intent);
        });

        goToColorMatch.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ColorMatch.class);
            startActivity(intent);
        });

    }


}