package com.flyfish.myapplication;

import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
import static android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Trace;
import android.provider.Settings.Global;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Created on 2020-05-10
 */
public class MyAccessibility extends AccessibilityService implements Runnable {
    private OkHttpClient client = new OkHttpClient();
    private List<String> keywords = new ArrayList<>();
    private Integer index = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("sdasd","qidong");
        new Thread(this).start();
        startIdlefish();

    }

    private void getKeyWords() {

        client.newCall(new Request.Builder().url("http://123.56.63.113/keywords").build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("sadasd", e + "");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<String> o = new Gson().fromJson(response.body().string(), new TypeToken<List<String>>() {
                }.getType());
                keywords.clear();
                keywords.addAll(o);
                Log.e("sad", new Gson().toJson(keywords) + "");

                //                Toast.makeText(MyAccessibility.this, "成功:"+keywords.size(), Toast.LENGTH_SHORT)
                //                .show();

            }
        });
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        startIdlefish();

    }


    private void startIdlefish() {
        Intent intent = new Intent();
        intent.setClassName("com.taobao.idlefish", "com.taobao.fleamarket.home.activity.InitActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("activity", getActivity() + "");

        if ("com.taobao.idlefish".equals(event.getPackageName())) {
            Log.e("asdasd",event.getClassName()+"===="+event);
            switch (event.getEventType()) {
                case TYPE_WINDOW_STATE_CHANGED:
                    switch (event.getClassName() + "") {
                        case "com.taobao.fleamarket.home.activity.MainActivity":
                            handleMainActivity();
                            break;
                    }
                case TYPE_WINDOW_CONTENT_CHANGED:
                    handleSearchTerm();
                    if("android.webkit.WebView".equals(event.getClassName())||"com.alibaba.wireless.security.open.middletier.fc.ui.ContainerActivity".equals(event.getClassName())){
                        performGlobalAction(GLOBAL_ACTION_BACK);
//                        Log.e("asd",getRootInActiveWindow()+"");

                    }






            }
        }


    }

    private void doEach(AccessibilityNodeInfo info) {
        if(info!=null ){
            for (int i = 0; i < info.getChildCount(); i++) {
                AccessibilityNodeInfo child = info.getChild(i);
                Log.e("asdasdasd",child.getText()+"");
                doEach(child);
            }
            Log.e("asdasdasd",info.getText()+"");

        }
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    private void handleSearchTerm() {
        if (getRootInActiveWindow() == null || keywords.size() == 0) {
            return;
        }
        try {
            List<AccessibilityNodeInfo> inputEdt =
                    getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.taobao.idlefish:id/search_term");
            if (inputEdt.size() > 0) {
                AccessibilityNodeInfo info = inputEdt.get(0);
                Bundle arguments = new Bundle();
                String keyword = keywords.get(index++ % keywords.size());
                arguments.putCharSequence(
                        AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, keyword);
                info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                List<AccessibilityNodeInfo> nodeInfosByText =
                        getRootInActiveWindow().findAccessibilityNodeInfosByText("搜索");
                if (nodeInfosByText.size() > 0) {
                    nodeInfosByText.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Thread.sleep(7000);
//                    move();
//                    Thread.sleep(1000);
                    performGlobalAction(GLOBAL_ACTION_BACK);

                }
                if (index > keywords.size() * 10) {
                    index = 0;
                }
                info.recycle();
            }
        } catch (Exception e) {
            Log.e("error", e + "");
        }


    }
    private void move(){
        Path mPath = new Path();//线性的path代表手势路径,点代表按下,封闭的没用
        //x y坐标  下面例子是往下滑动界面
        mPath.moveTo(100,100);//代表从哪个点开始滑动
        mPath.lineTo(120,500);//滑动到哪个点
        dispatchGesture(new GestureDescription.Builder().addStroke(new GestureDescription.StrokeDescription
                (mPath, 20, 500)).build(), new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Toast.makeText(MyAccessibility.this, "成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Toast.makeText(MyAccessibility.this, "取消", Toast.LENGTH_SHORT).show();

            }
        }, null);
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    private void handleMainActivity() {
        List<AccessibilityNodeInfo> searchView =
                getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.taobao.idlefish:id/tx_id");
        if (searchView.size() > 0) {
            AccessibilityNodeInfo info = searchView.get(0);
            while (true) {
                if (info == null) {
                    break;
                }
                if (info.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                    break;
                }
                info = info.getParent();
            }

        } else {
            Log.e("sadasd", "size===0");
        }

    }

    public static Activity getActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onInterrupt() {

    }

    @Override
    public void run() {
        while (true) {
            try {
                getKeyWords();

                Thread.sleep(500);
            } catch (Exception e) {
                Log.e("get keywords error", e + "");
            }
        }
    }
}
