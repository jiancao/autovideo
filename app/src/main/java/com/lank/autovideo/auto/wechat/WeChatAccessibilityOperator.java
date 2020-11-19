package com.lank.autovideo.auto.wechat;

import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import com.lank.autovideo.auto.AccessibilityOperator;
import com.lank.autovideo.tools.LogUtil;

import java.util.List;

public class WeChatAccessibilityOperator extends AccessibilityOperator {
    public static String pkgName = "com.tencent.mm";
    String plusId = "com.tencent.mm:id/dmw";
    String saoId="com.tencent.mm:id/f40";
    String imageId="com.tencent.mm:id/fah";
    String picId = "com.tencent.mm:id/dm6";
    String guanzhuId = "com.tencent.mm:id/c1i";
    String videoListId = "com.tencent.mm:id/ejr";
    String zanId = "com.tencent.mm:id/u1";

    private volatile static WeChatAccessibilityOperator instance;


    private WeChatAccessibilityOperator() {

    }

    public static WeChatAccessibilityOperator getInstance() {
        if (instance == null) {
            synchronized (WeChatAccessibilityOperator.class) {
                if (instance == null) {
                    instance = new WeChatAccessibilityOperator();
                }
            }
        }
        return instance;
    }

    @Override
    public void doAction() {
        LogUtil.d("-----------微信-------------");
        try {
            Thread.sleep(500);
            List<AccessibilityNodeInfo> list = findNodesById(plusId);
            LogUtil.d("list size:" + list.size());
            if (list != null && list.size() > 0) {
                list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                LogUtil.d("点击+号");
            }
            clickById(plusId);
            LogUtil.d("点击+号");
            Thread.sleep(500);
            List<AccessibilityNodeInfo> saoInfos = findNodesById(saoId);
            LogUtil.d("saoInfos size:" + saoInfos.size());
            try {
                if (saoInfos != null && saoInfos.size() > 0) {
                    saoInfos.get(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    LogUtil.d("点击扫一扫");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(500);
            clickById(imageId);
            LogUtil.d("点击图片");
            Thread.sleep(500);
            List<AccessibilityNodeInfo> picInfos = findNodesById(picId);
            LogUtil.d("picInfos size:"+picInfos.size());
            if (picInfos != null && picInfos.size() > 0) {
                picInfos.get(0).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                LogUtil.d("识别二维码图片");

            }
            Thread.sleep(2000);
            clickById(guanzhuId);
            LogUtil.d("点击关注");
            List<AccessibilityNodeInfo> videoListInfos = findNodesById(videoListId);
            if (videoListInfos != null && videoListInfos.size() > 0) {
                videoListInfos.get(0).getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                LogUtil.d("点击第一个视频");
            }
            Thread.sleep(500);
            List<AccessibilityNodeInfo> zanListInfos = findNodesById(zanId);
            if (zanListInfos != null && zanListInfos.size() > 0) {
                zanListInfos.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                LogUtil.d("点击第一个赞");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
