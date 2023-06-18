package com.wohoocreators.dynamicisland;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


@SuppressLint("MissingInflatedId")
public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    Button active;

    @Override
    protected void onResume() {
        super.onResume();

        if (MainActivity.this.isAccessibilityServiceEnabled()&&MainActivity.this.isNotificationServiceEnabled()){
            active.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView=new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-6850472818552165/2083591525");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // Get preferences
        SharedPreferences prefs = getSharedPreferences("com.wohoocreators.dynamicisland", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();


        // Update setting switches and bars
        active = findViewById(R.id.grant);;
        Switch deletefrombar = findViewById(R.id.deletefrombar);
        ProgressBar x = findViewById(R.id.x);
        ProgressBar x2 = findViewById(R.id.x2);
        ProgressBar y = findViewById(R.id.y);
        ProgressBar y2 = findViewById(R.id.y2);
        ProgressBar width = findViewById(R.id.width);
        ProgressBar width2 = findViewById(R.id.width2);
        ProgressBar c = findViewById(R.id.heightclosed);
        ProgressBar o = findViewById(R.id.heightopen);



        // Set listeners
        findViewById(R.id.donate).setOnClickListener(view -> {
            // Open a url
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/ethanporcaro"));
            startActivity(browserIntent);
        });

        findViewById(R.id.reset).setOnClickListener(view -> {
            // Reset UI
            deletefrombar.setChecked(false);
            x.setProgress(0);
            x2.setProgress(0);
            y.setProgress(5);
            y2.setProgress(-220);
            width.setProgress(319);
            width2.setProgress(1014);
            c.setProgress(113);
            o.setProgress(563);

            // Reset preferences
            editor.putBoolean("deletefrombar", false);
            editor.putInt("x", 0);
            editor.putInt("x2", 0);
            editor.putInt("y", 5);
            editor.putInt("y2", -220);
            editor.putInt("width", 319);
            editor.putInt("width2", 1014);
            editor.putInt("heightclosed", 113);
            editor.putInt("heightopen", 563);
            editor.apply();

        });

        x.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                editor.putInt("x", x.getProgress());
                editor.apply();
            }
            return false;
        });

        x2.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                editor.putInt("x2", x2.getProgress());
                editor.apply();
            }
            return false;
        });

        y.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                editor.putInt("y", y.getProgress());
                editor.apply();
            }
            return false;
        });

        y2.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                editor.putInt("y2", y2.getProgress());
                editor.apply();
            }
            return false;
        });

        width.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                editor.putInt("width", width.getProgress());
                editor.apply();
            }
            return false;
        });

        width2.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                editor.putInt("width2", width2.getProgress());
                editor.apply();
            }
            return false;
        });

        c.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                editor.putInt("heightclosed", c.getProgress());
                editor.apply();
            }
            return false;
        });

        o.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                editor.putInt("heightopen", o.getProgress());
                editor.apply();
            }
            return false;
        });

        deletefrombar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("deletefrombar", isChecked);
            editor.apply();
        });

        ((Button) findViewById(R.id.reset)).setPressed(true);
        ((Button) findViewById(R.id.reset)).setPressed(false);

        active.setOnClickListener((compoundButton) -> {

            if (!MainActivity.this.isNotificationServiceEnabled()) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("We need permission...")
                        .setMessage("To want permission to read notifications, so we can show them on your display and control music.")

                        .setPositiveButton("Continue", (dialog, which) -> {

                            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                            startActivity(intent);
                        })



                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Back", null)
                        .show();
                active.setText("One more step");

            } else if(!MainActivity.this.isAccessibilityServiceEnabled()) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Acessibility")
                        .setMessage("This app needs to be activated in Acessibility to show Dynamic Island View. Please click Continue find and click Dynamic Islanfd (it may be in installed apps or Downloaded service with some devices) to do it.")

                        .setPositiveButton("Continue", (dialog, which) -> {

                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivity(intent);
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("Back", null)
                        .show();

            } else {

                // Toast to show that the app has permissions
                Toast.makeText(MainActivity.this, "You've already given this app permissions!", Toast.LENGTH_SHORT).show();
            }

        });

    }


    // Check if we have notification access
    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private boolean isAccessibilityServiceEnabled() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (HPDisplay.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    }

