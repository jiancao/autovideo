package com.lank.autovideo;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lank.autovideo.auto.livelite.LiveLiteAccessibilityOperator;
import com.lank.autovideo.auto.ugc.UgcAccessibilityOperator;
import com.lank.autovideo.auto.wechat.WeChatAccessibilityOperator;
import com.lank.autovideo.service.MyAccessibilityService;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {


    Button btn_ugc, btn_ugc_live, btn_wechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        btn_ugc = findViewById(R.id.btn_ugc);
        btn_ugc_live = findViewById(R.id.btn_ugc_live);
        btn_wechat = findViewById(R.id.btn_wechat);


        // Set up the user interaction to manually show or hide the system UI.
        btn_ugc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getPackageManager();
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage(UgcAccessibilityOperator.pkgName);
                if (intent == null) {
                    Toast.makeText(FullscreenActivity.this, "未安装", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }
            }
        });
        btn_ugc_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage(LiveLiteAccessibilityOperator.pkgName);
                if (intent == null) {
                    Toast.makeText(FullscreenActivity.this, "未安装", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }
            }
        });
        btn_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                Intent intent = new Intent();
                intent = packageManager.getLaunchIntentForPackage(WeChatAccessibilityOperator.pkgName);
                if (intent == null) {
                    Toast.makeText(FullscreenActivity.this, "未安装", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }
            }
        });

        showAccessibilityDialog();
    }

    private boolean isAccessibilitySettingsOn(Context mContext, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void showAccessibilityDialog() {
        if (isAccessibilitySettingsOn(FullscreenActivity.this, MyAccessibilityService.class)) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
        builder.setMessage("辅助功能未开启");
        builder.setPositiveButton("前往开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

}
