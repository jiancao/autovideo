package com.lank.autovideo.auto.livelite;

import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import com.lank.autovideo.auto.AccessibilityOperator;
import com.lank.autovideo.tools.LogUtil;

import java.util.List;

import androidx.annotation.RequiresApi;

public class LiveLiteAccessibilityOperator extends AccessibilityOperator {
    public static String pkgName = "com.ss.android.ugc.livelite";
    String VideoVpId = "com.ss.android.ugc.livelite:id/ag";//视频
    String videoContentId = "com.ss.android.ugc.livelite:id/a3b";//广告视频

    private volatile static LiveLiteAccessibilityOperator instance;

    boolean isVideoPlay;//是否视频播放

    private LiveLiteAccessibilityOperator() {

    }

    public static LiveLiteAccessibilityOperator getInstance() {
        if (instance == null) {
            synchronized (LiveLiteAccessibilityOperator.class) {
                if (instance == null) {
                    instance = new LiveLiteAccessibilityOperator();
                }
            }
        }
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void doAction() {
        LogUtil.d("-----------火山极速版-------------");
        //刷视频
        List<AccessibilityNodeInfo> list = findNodesById(VideoVpId);
        if (list != null && list.size() > 0 && !isVideoPlay) {
            LogUtil.d("火山视频列表");
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
                    LogUtil.d("广告视频");
                    Thread.sleep(6 * 1000);
                    clickById(videoContentId);
                    Thread.sleep(2000);
                } else {
                    LogUtil.d("本视频播放秒数=" + (10+playSeconds));
                    Thread.sleep(playSeconds * 1000);
                }

                scoll(500, 1500);
            } catch (Exception e) {
                LogUtil.e(e.toString());
                e.printStackTrace();
                scoll(500, 1500);
            }
        }
    }
}
