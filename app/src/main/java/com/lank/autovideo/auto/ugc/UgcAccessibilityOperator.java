package com.lank.autovideo.auto.ugc;

import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lank.autovideo.auto.AccessibilityOperator;
import com.lank.autovideo.tools.LogUtil;

import java.util.List;

import androidx.annotation.RequiresApi;

public class UgcAccessibilityOperator extends AccessibilityOperator {
    public static String pkgName = "com.ss.android.ugc.aweme.lite";
    String VideoVpId = "com.ss.android.ugc.aweme.lite:id/efw";//视频
    String videoContentId = "com.ss.android.ugc.aweme.lite:id/blc";//广告视频

    private volatile static UgcAccessibilityOperator instance;

    boolean isVideoPlay;//是否视频播放

    private UgcAccessibilityOperator() {

    }

    public static UgcAccessibilityOperator getInstance() {
        if (instance == null) {
            synchronized (UgcAccessibilityOperator.class) {
                if (instance == null) {
                    instance = new UgcAccessibilityOperator();
                }
            }
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void doAction() {
        LogUtil.d("-----------抖音极速版-------------");
        //刷视频
        List<AccessibilityNodeInfo> list = findNodesById(VideoVpId);
        if (list != null && list.size() > 0 && !isVideoPlay) {
            LogUtil.d("抖音视频列表");
            isVideoPlay = true;
        } else {
            isVideoPlay = false;
        }

        if (isVideoPlay) {
            isVideoPlay=false;
            long playSeconds = (long) (Math.random() * 5)+1;
            try {
                Thread.sleep(10000);
                List<AccessibilityNodeInfo> contentNodeInfos = findNodesById(videoContentId);
                if (contentNodeInfos != null && contentNodeInfos.size() > 0) {
                    playSeconds=10;
                    LogUtil.d("广告视频");
                } else {
                    LogUtil.d("本视频播放秒数=" + (10+playSeconds));
                }
                Thread.sleep(playSeconds * 1000);
                scoll(100, 1500);
            } catch (Exception e) {
                LogUtil.e(e.toString());
                e.printStackTrace();
                scoll(100, 1500);
            }
        }
    }
}
