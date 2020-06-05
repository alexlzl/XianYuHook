package com.flyfish.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;

import static de.robv.android.xposed.XposedHelpers.findAndHookConstructor;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created on 2020-05-02
 */

public class Search implements IXposedHookLoadPackage {
    public static Object xhsHttpInterceptor;
    public static Boolean isStart = false;
    public static Boolean isDetailStart = false;
    public static Boolean isHookStart = false;
    public static Object signImpl = null;
    public static List<String> keywords = new ArrayList<>();

    static {
        keywords.add("iphone");
    }

    public static Object did;
    public static Object modle;

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        handle_xianyu(lpparam);

    }

    private void handle_xianyu(LoadPackageParam lpparam) throws ClassNotFoundException {
        if (lpparam.packageName.equals("com.taobao.idlefish")) {
            handle_i(lpparam);
            handle_k(lpparam);
            hookLogin(lpparam);
        }
    }

    private void handSearch(LoadPackageParam lpparam){


    }

    private void handle_i(LoadPackageParam lpparam) throws ClassNotFoundException {
        XposedBridge.log("hook======handle_i");
        Class<?> aClass = lpparam.classLoader.loadClass("mtopsdk.mtop.global.SwitchConfig");

        findAndHookMethod(aClass, "rW", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });

        findAndHookMethod(aClass, "rX", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });

        findAndHookMethod("mtopsdk.mtop.domain.MtopResponse", lpparam.classLoader, "parseJsonByte",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        String api = XposedHelpers.getObjectField(param.thisObject, "api").toString();

                        if ("mtop.taobao.idle.search.glue".equals(api)) {
                            byte[] jsonObject = (byte[]) XposedHelpers.getObjectField(param.thisObject, "bytedata");
                            if (jsonObject != null && jsonObject.length > 0) {
                                log(new String(jsonObject));
                            }

                        }
                    }
                });


    }

    private void hookLogin(final LoadPackageParam lpparam)throws ClassNotFoundException{
        final Class<?> alarmClacc = lpparam.classLoader.loadClass("com.alibaba.wireless.security.open.middletier.fc.ui.ContainerActivity");
        findAndHookMethod(alarmClacc, "onCreate", Bundle.class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("ContainerActivity==执行=====back====afterHookedMethod");
                //不能通过Class.forName()来获取Class ，在跨应用时会失效
                Method finish=alarmClacc.getDeclaredMethod("onBackPressed",null);
                finish.setAccessible(true);
                finish.invoke(param.thisObject,null);
                param.setResult(null);
            }
        });
        final Class<?> loginClass = lpparam.classLoader.loadClass("com.ali.user.mobile.login.ui.UserLoginActivity");
        findAndHookMethod(loginClass, "onCreate", Bundle.class,new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("UserLoginActivity==执行=====back====afterHookedMethod");
                //不能通过Class.forName()来获取Class ，在跨应用时会失效
                Method finish=loginClass.getDeclaredMethod("onBackPressed",null);
                finish.setAccessible(true);
                finish.invoke(param.thisObject,null);
                param.setResult(null);
            }
        });
    }


    private void handle_k(LoadPackageParam lpparam) throws ClassNotFoundException {
        XposedBridge.log("执行handle_k");
        Class<?> SignImpl = lpparam.classLoader.loadClass("mtopsdk.security.InnerSignImpl");
        Class<?> Config = lpparam.classLoader.loadClass("mtopsdk.mtop.global.MtopConfig");
        Class<?> UTUtdid = lpparam.classLoader.loadClass("com.ta.utdid2.device.UTUtdid");
        final Class<?> UmidUtils = lpparam.classLoader.loadClass("com.ta.audid.utils.UmidUtils");
        final Class<?> AppUtdid = lpparam.classLoader.loadClass("com.ta.audid.device.AppUtdid");
        final Class<?> UtdidKeyFile = lpparam.classLoader.loadClass("com.ta.audid.upload.UtdidKeyFile");
        final Class<?> PhoneInfoUtils = lpparam.classLoader.loadClass("com.ta.utdid2.android.utils.PhoneInfoUtils");
        final Class<?> UtdidContentUtil = lpparam.classLoader.loadClass("com.ta.audid.store.UtdidContentUtil");
        findAndHookMethod(UtdidContentUtil, "ct", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                log("utdid === " + param.getResult());
            }
        });
        findAndHookMethod(SignImpl, "getUnifiedSign", HashMap.class, HashMap.class, String.class, String.class,
                boolean.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        signImpl = param.thisObject;
                        //                        search("iphonex");
                        super.beforeHookedMethod(param);
                        //
                        //                                     log(new Gson().toJson(param.args));
                        XposedBridge.log("before===getUnifiedSign");
                        runDetail();
                        runSearch();


                    }
                });
        findAndHookConstructor(UTUtdid, Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                did = param.thisObject;


            }
        });
        findAndHookMethod(UTUtdid, "readUtdid", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult("");
            }
        });
        findAndHookMethod(PhoneInfoUtils, "getImei", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(getRandomNumber(11));
            }
        });
        XposedHelpers.findAndHookMethod(UmidUtils, "getUmidToken", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                log(param.getResult() + "----------");

            }

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.setStaticObjectField(UmidUtils, "mUmidToken", getRandomString("", 32));
                super.beforeHookedMethod(param);

            }
        });
        XposedHelpers.findAndHookMethod(AppUtdid, "lV", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);


                modle = param.thisObject;


            }

        });
        XposedHelpers.findAndHookMethod(UtdidKeyFile, "ap", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(true);
            }
        });


    }
    private void runDetail() {
        synchronized (Search.class) {
            XposedBridge.log("run detail");
            if (!isDetailStart) {
                isDetailStart = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String itemId = "617713196977";

                            log("requestdetil");

                            searchDetail(itemId);
                        }
                    }
                }).start();
            }
        }
    }
    private void searchDetail(String itemId) {
        DetailInfo detailInfo = new DetailInfo(itemId);
        String term = new Gson().toJson(detailInfo);
        HashMap<String, String> p =
                new Gson().fromJson(term,
                        new TypeToken<HashMap<String, String>>() {
                        }.getType());
        HashMap<String, String> signs = (HashMap<String, String>) XposedHelpers
                .callMethod(signImpl, "getUnifiedSign", p, null, "21407387",
                        null,
                        false);
        requestDetail(detailInfo, signs);
    }
    private void runSearch() {
        synchronized (Search.class) {
            if (!isStart) {
                isStart = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            getKeyWords();
                            Log.e("keywords", new Gson().toJson(keywords));
                            for (String keyword : keywords) {

                                try {
                                    search(keyword);
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    log(e + "");
                                }

                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();

                            }

                        }
                    }
                }).start();
            }
        }

    }
    public static String getRandomString(String extra, int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" + extra;
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < length; i++) {
            res.append(str.charAt(randBetween(0, str.length())));
        }
        return res.toString();
    }

    public static int randBetween(int min, int max) {
        int range = Math.abs(min - max);
        if (range == 0) {
            return min;
        }
        int pos = new Random().nextInt(range);
        return Math.min(min, max) + pos;
    }


    private void getKeyWords() {
        try {
            String string =
                    new OkHttpClient().newCall(new Builder().url("http://123.56.63.113/keywords").build()).execute()
                            .body()
                            .string();
            List<String> o = new Gson().fromJson(string, new TypeToken<List<String>>() {
            }.getType());
            if (o != null && o.size() > 0) {
                keywords.clear();
                keywords.addAll(o);
            }
            XposedBridge.log(keywords + "");

        } catch (Exception e) {
            XposedBridge.log("error" + e.getMessage() + "");
        }


    }






    private void requestDetail(DetailInfo searchInfo, HashMap<String, String> signs) {
        XposedBridge.log("请求详情==============");
        String sgext = URLEncoder.encode(signs.get("x-sgext"));
        String umt = URLEncoder.encode(signs.get("x-umt"));
        String sign = URLEncoder.encode(signs.get("x-sign"));
        String wua = URLEncoder.encode(signs.get("x-mini-wua"));
        OkHttpClient client = new OkHttpClient.Builder().build();
        Headers headers = new Headers.Builder()
                .add("x-sgext", sgext)
                .add("umid", umt)
                .add("x-sign", sign)
                .add("x-nettype", "WIFI")
                .add("x-pv", "6.3")
                .add("x-nq", "WIFI")
                .add("x-features", searchInfo.getXfeatures())
                .add("x-app-conf-v", "0")
                .add("x-mini-wua", wua)
                .add("x-t", searchInfo.getT())
                .add("x-bx-version", "6.5.5")
                .add("f-refer", "mtop")
                .add("x-ttid", searchInfo.getTtid())
                .add("x-app-ver", "6.6.90")
                .add("x-umt", "NM5Lw1hLOt31GzVyJohiRsjM1PjWbvr4")

                .add("x-utdid", searchInfo.getUtdid())
                .add("x-appkey", searchInfo.getAppKey())
                .add("x-devid", searchInfo.getDeviceId())
                .add("user-agent", "MTOPSDK%2F3.1.1.7+%28Android%3B9%3BGoogle%3BPixel%29")
                .build();
        FormBody formBody = new FormBody.Builder().add("data", searchInfo.getData()).build();
        //        HashMap<String, String> m = new HashMap<>();
        //        m.put("data",searchInfo.getData());
        //        RequestBody requestBody = RequestBody
        //                .create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), new Gson()
        //                .toJson(m));
        client.newCall(new Request.Builder().url("http://acs.m.taobao.com/gw/mtop.taobao.idle.awesome.detail/1.0/")
                .method("POST", formBody).headers(headers).build()).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        log(e + "失败");

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        log("车祸" + response.body().string());
                    }
                });

    }

    private void search(String keyword) throws IOException {
        SearchInfo searchInfo = new SearchInfo(keyword);
        String term = new Gson().toJson(searchInfo);
        HashMap<String, String> p =
                new Gson().fromJson(term,
                        new TypeToken<HashMap<String, String>>() {
                        }.getType());
        //        XposedBridge.log(term);
        p.put("sid", null);
        p.put("uid", null);
        HashMap<String, String> signs = (HashMap<String, String>) XposedHelpers
                .callMethod(signImpl, "getUnifiedSign", p, null, "21407387",
                        null,
                        false);
        request(searchInfo, signs);
    }

    private void request(SearchInfo searchInfo, HashMap<String, String> signs) {
        String sgext = URLEncoder.encode(signs.get("x-sgext"));
        String umt = URLEncoder.encode(signs.get("x-umt"));
        String sign = URLEncoder.encode(signs.get("x-sign"));
        String wua = URLEncoder.encode(signs.get("x-mini-wua"));
        OkHttpClient client = new OkHttpClient.Builder().build();
        Headers headers = new Headers.Builder()
                .add("x-sgext", sgext)
                .add("umid", umt)
                .add("x-sign", sign)
                //                .add("x-sid", searchInfo.getSid())
                //                .add("x-uid", searchInfo.getUid())
                .add("x-nettype", "WIFI")
                .add("x-pv", "6.3")
                .add("x-nq", "WIFI")
                .add("x-features", searchInfo.getXfeatures())
                .add("x-app-conf-v", "0")
                .add("x-mini-wua", wua)
                .add("x-t", searchInfo.getT())
                .add("x-bx-version", "6.5.5")
                .add("f-refer", "mtop")
                .add("x-ttid", searchInfo.getTtid())
                .add("x-app-ver", "6.6.90")
                //                .add("x-umt", "NM5Lw1hLOt31GzVyJohiRsjM1PjWbvr4")
                .add("x-utdid", URLEncoder.encode(searchInfo.getUtdid()))
                .add("x-appkey", searchInfo.getAppKey())
                .add("x-devid", searchInfo.getDeviceId())
                .add("user-agent", "MTOPSDK%2F3.1.1.7+%28Android%3B9%3BGoogle%3BPixel%29")
                .build();
        FormBody formBody = new FormBody.Builder().add("data", searchInfo.getData()).build();
        String url = "http://acs.m.taobao.com/gw/mtop.taobao.idle.search.glue/1.0/";
        client.newCall(new Request.Builder().url(url)
                .method("POST", formBody).headers(headers).build()).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        log(e + "失败");

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        log("车祸" + response.body().string());
                    }
                });

    }

    private void log(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new OkHttpClient().newCall(new Request.Builder().url("http://192.168.1.5:8080/logs")
                            .post(new FormBody.Builder().add("log", msg).build()).build()).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        XposedBridge.log(msg);
    }


    public static String getRandomNumber(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(getRandomNumber());
        }
        return sb.toString();
    }

    public static char getRandomNumber() {
        return (char) ('1' + new Random().nextInt(9));
    }


}
