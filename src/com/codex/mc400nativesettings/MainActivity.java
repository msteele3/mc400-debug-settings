package com.codex.mc400nativesettings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private TextView status;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (24 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);

        TextView intro = new TextView(this);
        intro.setText("Open the projector's Android developer settings. If Wireless debugging appears, choose Pair device with pairing code.");
        intro.setTextSize(18);
        layout.addView(intro);

        addButton(layout, "1. Open Android TV Developer Options", new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.tv.settings",
                    "com.android.tv.settings.system.development.DevelopmentActivity"));
                launch(intent);
            }
        });

        addButton(layout, "2. Open standard Developer Options", new View.OnClickListener() {
            @Override public void onClick(View v) {
                launch(new Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS"));
            }
        });

        addButton(layout, "3. Try Wireless Debugging screen", new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.settings",
                    "com.android.settings.Settings$WirelessDebuggingActivity"));
                launch(intent);
            }
        });

        status = new TextView(this);
        status.setTextSize(16);
        status.setText("Try button 1 first. This app only opens Settings screens and requires no permissions.");
        layout.addView(status);
        setContentView(layout);
    }

    private void addButton(LinearLayout layout, String label, View.OnClickListener action) {
        Button button = new Button(this);
        button.setText(label);
        button.setOnClickListener(action);
        layout.addView(button);
    }

    private void launch(Intent intent) {
        try {
            startActivity(intent);
            status.setText("Settings opened. Look for Wireless debugging, then Pair device with pairing code.");
        } catch (Exception e) {
            status.setText("This screen could not be opened: " + e.getClass().getSimpleName());
        }
    }
}
