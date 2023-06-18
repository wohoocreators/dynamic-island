package com.wohoocreators.dynamicisland;

import static com.wohoocreators.dynamicisland.UIState.largeParams;
import static com.wohoocreators.dynamicisland.UIState.smallParams;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.RequiresApi;

import java.util.Objects;


public class HPDisplay extends AccessibilityService {

    public View display;
    public View pill;
    public PendingIntent clickAction;

    public final static int DEFAULT_DELAY = 3000;
    public int closeDelay = DEFAULT_DELAY;
    private final Handler closeRunnable = new Handler();

    public NotificationHandler notificationHandler = null;
    public WindowManager windowManager;
    public MediaManager mediaManager = null;
    public Utils utils = null;
    public UIState uiState = null;

    public HPDisplay() {
    }


    @Override
    public void onInterrupt() {
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onServiceConnected() {
        create();
    }


    @SuppressLint({"ClickableViewAccessibility", "InflateParams", "UseCompatLoadingForDrawables"})
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void create() {

        // Create the overlay window and add the view
        windowManager = (WindowManager) this.getSystemService(Service.WINDOW_SERVICE);

        LayoutInflater li = LayoutInflater.from(this);
        this.display = li.inflate(R.layout.popup, null);
        this.pill = li.inflate(R.layout.popup, null);


        smallParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        largeParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        windowManager.addView(this.pill, smallParams);
        windowManager.addView(this.display, largeParams);

        this.pill.findViewById(R.id.label).setVisibility(View.GONE); // Don't show pill text
        this.pill.findViewById(R.id.description).setVisibility(View.GONE);
        this.display.setVisibility(View.GONE);
        this.display.findViewById(R.id.button).setClickable(false); // Don't allow display to be clicked, only pill (for now)
        this.display.findViewById(R.id.icon2).setVisibility(View.GONE); // Don't show display right icon


        // Initialize the different components
        MediaManager mediaManager = new MediaManager(this);
        Gestures gestures = new Gestures(this, this.display, this.pill, mediaManager);

        // Initialize the UI
        uiState = new UIState(this, UIState.DEFAULT_TITLE, UIState.DEFAULT_DESCRIPTION,
                getDrawable(R.drawable.checkmark), UIState.ICON_BLANK, UIState.ICON_BLANK, UIState.SHAPE_NOCHANGE);

        uiState.apply();

        gestures.setListeners();

        // Initialize more components
        this.notificationHandler = new NotificationHandler(this);
        this.mediaManager = new MediaManager(this);
        this.utils = new Utils(this);

    }

    public void waitToClose() {

        closeDelay = DEFAULT_DELAY;

        closeRunnable.removeCallbacksAndMessages(null);
        closeRunnable.postDelayed(() -> {
            UIState uiState = this.uiState;
            uiState.shape = UIState.SHAPE_CLOSED;
            uiState.apply();
        }, closeDelay);

    }

    @SuppressLint("RtlHardcoded")
    public void updatePostion() {
        int x = utils.getSettingInt("x");
        int y = utils.getSettingInt("y");
        int y2 = utils.getSettingInt("y2");
        int c = utils.getSettingInt("heightclosed");
        int o = utils.getSettingInt("heightopen");
        int w = utils.getSettingInt("width");
        int w2 = utils.getSettingInt("width2");

        Log.d("HPDisplay", "x: " + x + " y: " + y + " y2: " + y2 + " c: " + c + " o: " + o + " w: " + w + " w2: " + w2);

        WindowManager.LayoutParams params1 = smallParams;
        WindowManager.LayoutParams params2 = largeParams;

        params1.x = x;
        params1.y = y;
        params1.width = w;
        params1.height = c;

        params2.x = x;
        params2.y = y2;
        params2.width = w2;
        params2.height = o;

        params1.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        params2.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

        windowManager.updateViewLayout(pill, params1);
        windowManager.updateViewLayout(display, params2);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String eventText = "";

        switch (event.getEventType()) {
            /*
                You can use catch other events like touch and focus

                case AccessibilityEvent.TYPE_VIEW_CLICKED:
                     eventText = "Clicked: ";
                     break;
                case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                     eventText = "Focused: ";
                     break;
            */
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                String packageName = event.getPackageName().toString();
                if (packageName.startsWith("com.whatsapp")) {
                    eventText = eventText + event.getText() + " || " + event.getPackageName();
                }
                break;
        }

        if (!eventText.isEmpty()) {

            Log.v("event", eventText);
        }

        try {
            notificationHandler.readNotification(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            updatePostion();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Set a delay because the device needs a moment to update the media state
            new Handler().postDelayed(() -> mediaManager.updateMedia(), 5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO: Make this work
        /*
        // Get the window orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            pill.setVisibility(View.GONE);
            display.setVisibility(View.GONE);
        } else {
            // In portrait
            pill.setVisibility(View.VISIBLE);
            if (uiState.shape == UIState.SHAPE_OPEN) {
                display.setVisibility(View.VISIBLE);
            } else {
                display.setVisibility(View.GONE);
            }
        }
        */

    }
}