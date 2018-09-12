package com.example.aatest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.example.aatest.R;
import com.example.aatest.activity.base.ActivityBase;

public class Splash extends Activity {
    private LinearLayout layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        layout=(LinearLayout)findViewById(R.id.splashLayout);
        startAnimation();
    }

    private void startAnimation() {
        AlphaAnimation animation=new AlphaAnimation(0.1f,1.0f);
        animation.setDuration(1000);
        layout.setAnimation(animation);
        layout.startAnimation(animation);
        new Handler().postDelayed(new loadSplash(),1000);
    }

    private class loadSplash implements Runnable {
        @Override
        public void run() {
            Intent i=new Intent(Splash.this,ActivityMain.class);
            startActivity(i);
            finish();
        }
    }
}
