package com.example.ExpenseTracker;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    Animation topAnim, bottomAnim;
    ImageView logo_image;
    TextView logo_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logo_image = findViewById(R.id.logo_image_id);
        logo_text = findViewById(R.id.logo_text_id);

        logo_image.setAnimation(topAnim);
        logo_text.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}