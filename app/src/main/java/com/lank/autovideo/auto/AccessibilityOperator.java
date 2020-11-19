package com.lank.autovideo.auto;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.lank.autovideo.tools.LogUtil;

import java.util.List;

public abstract class AccessibilityOperator {

    Context mContext;
    AccessibilityEvent mAccessibilityEvent;
    AccessibilityService mAccessibilityService;

    public void setContext(Context context) {
        mContext = context;
    }

    public void setAccessibilityService(AccessibilityService accessibilityService) {
        if (accessibilityService != null && mAccessibilityService == null) {
            mAccessibilityService = accessibilityService;
        }
    }

    public void setAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        mAccessibilityEvent = accessibilityEvent;
    }

    public abstract void doAction();


    private AccessibilityNodeInfo getRootNodeInfo() {
        AccessibilityEvent curEvent = mAccessibilityEvent;
        AccessibilityNodeInfo nodeInfo = null;
        if (Build.VERSION.SDK_INT >= 16) {
            // 建议使用getRootInActiveWindow，这样不依赖当前的事件类型
            if (mAccessibilityService != null) {
                nodeInfo = mAccessibilityService.getRootInActiveWindow();
            }
            // 下面这个必须依赖当前的AccessibilityEvent
//            nodeInfo = curEvent.getSource();
        } else {
            nodeInfo = curEvent.getSource();
        }
        return nodeInfo;
    }

    public List<AccessibilityNodeInfo> findNodesById(String viewId) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            if (Build.VERSION.SDK_INT >= 18) {
                return nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
            }
        }
        return null;
    }

    /**
     * 根据View的ID搜索符合条件的节点,精确搜索方式;
     * 这个只适用于自己写的界面，因为ID可能重复
     * api要求18及以上
     *
     * @param viewId
     * @return 是否点击成功
     */
    public boolean clickById(String viewId) {
        return performClick(findNodesById(viewId));
    }

    private boolean performClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                // 进行模拟点击
                if (node.isClickable()) {
                    LogUtil.d("Click：" + node.getClassName());
                    return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else if (node.getParent().isClickable()) {
                    LogUtil.d("Click：" + node.getParent().getClassName());
                    return node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
        return false;
    }

    public boolean longClickById(String viewId) {
        return performLongClick(findNodesById(viewId));
    }

    private boolean performLongClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                // 进行模拟点击
                if (node.isClickable()) {
                    LogUtil.d("Long Click：" + node.getClassName());
                    return node.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                } else if (node.getParent().isClickable()) {
                    LogUtil.d("Long Click：" + node.getParent().getClassName());
                    return node.getParent().performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
                }
            }
        }
        return false;
    }

    public boolean clickBackKey() {
        return performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    private boolean performGlobalAction(int action) {
        return mAccessibilityService.performGlobalAction(action);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scoll(int x, int y) {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(100, 0);
        GestureDescription gestureDescription = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 200L, 800L, false)).build();
        }
        mAccessibilityService.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
            }
        }, new Handler(Looper.getMainLooper()));

    }

    /**
     * 根据Text搜索所有符合条件的节点, 模糊搜索方式
     */
    public List<AccessibilityNodeInfo> findNodesByText(String text) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            return nodeInfo.findAccessibilityNodeInfosByText(text);
        }
        return null;
    }

    public boolean clickByText(String text) {
        return performClick(findNodesByText(text));
    }

}
