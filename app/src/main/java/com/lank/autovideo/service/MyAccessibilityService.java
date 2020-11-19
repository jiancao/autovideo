package com.lank.autovideo.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import com.lank.autovideo.auto.AccessibilityOperator;
import com.lank.autovideo.auto.livelite.LiveLiteAccessibilityOperator;
import com.lank.autovideo.auto.ugc.UgcAccessibilityOperator;
import com.lank.autovideo.auto.wechat.WeChatAccessibilityOperator;
import com.lank.autovideo.tools.LogUtil;

import androidx.annotation.RequiresApi;

public class MyAccessibilityService extends AccessibilityService {
    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.packageNames = new String[]{UgcAccessibilityOperator.pkgName, LiveLiteAccessibilityOperator.pkgName, WeChatAccessibilityOperator.pkgName};// 监控的app
        serviceInfo.notificationTimeout = 100;
        serviceInfo.flags = serviceInfo.flags | AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        setServiceInfo(serviceInfo);
    }

    @Override
    public void onInterrupt() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
        // 获取包名
        String pkgName = event.getPackageName().toString();
        LogUtil.d("pkgName=" + pkgName);
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                AccessibilityOperator operator;
                if (pkgName.equals(UgcAccessibilityOperator.pkgName)) {
                    operator = UgcAccessibilityOperator.getInstance();
                }else if(pkgName.equals(WeChatAccessibilityOperator.pkgName)){
                    operator = WeChatAccessibilityOperator.getInstance();
                } else{
                    operator = LiveLiteAccessibilityOperator.getInstance();
                }
                operator.setContext(this);
                operator.setAccessibilityService(this);
                operator.setAccessibilityEvent(event);
                operator.doAction();
                break;
        }
    }
}
