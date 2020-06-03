package com.flyfish.myapplication;

import java.util.Random;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

/**
 *
 * Created on 2020-05-19
 */
public class DetailInfo {

    /**
     * data :
     * deviceId : ApSXKgvuIgw7YzImdDN-J_vwPQEnlJdJ24bSQwR8OD0v
     * sid : 1a8a754630af722fed71cdcc692de39d
     * uid : 2200613420057
     * x-features : 27
     * appKey : 21407387
     * api : mtop.taobao.idle.search.glue
     * utdid : XsI4D5g4bBkDAGlc6F/VOERf
     * ttid : 10006261@fleamarket_android_6.6.90
     * t : 1589817539
     * v : 1.0
     */

    private String data;
    private String deviceId = "ApSXKgvuIgw7YzImdDN-J_vwPQEnlJdJ24bSQwR8OD0v";
    @SerializedName("x-features")
    private String xfeatures = "27";
    private String appKey = "21407387";
    private String api = "mtop.taobao.idle.awesome.detail";
    private String utdid = "XsI4D5g4bBkDAGlc6F/VOERf";
    private String ttid = "10006261@fleamarket_android_6.6.90";
    private String t;
    private String v = "1.0";

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

    {
        t = new Integer(String.valueOf(System.currentTimeMillis() / 1000)) + "";
        deviceId = getRandomString("-_0123456789", 44);
        utdid = "XYMyPdHcVGEDAFHziq" + getRandomString("", 6);
    }

    public DetailInfo(String itemId) {
        DetailItem detailInfo = new DetailItem();
        detailInfo.setItemId(itemId);
        data = detailInfo.toString();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getXfeatures() {
        return xfeatures;
    }

    public void setXfeatures(String xfeatures) {
        this.xfeatures = xfeatures;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getUtdid() {
        return utdid;
    }

    public void setUtdid(String utdid) {
        this.utdid = utdid;
    }

    public String getTtid() {
        return ttid;
    }

    public String getT() {
        return t;
    }

    public void setTtid(String ttid) {
        this.ttid = ttid;
    }


    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}

class DetailItem {


    /**
     * gps : 40.086679,116.299072
     * itemId : 617619343782
     * latitude : 40.086679
     * longitude : 116.299072
     * needSimpleDetail : false
     */

    private String itemId;

    private boolean needSimpleDetail = false;



    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public boolean isNeedSimpleDetail() {
        return needSimpleDetail;
    }

    public void setNeedSimpleDetail(boolean needSimpleDetail) {
        this.needSimpleDetail = needSimpleDetail;
    }

    @Override
    public String toString() {

        String s = new Gson().toJson(this);
        TreeMap<String, Object> treeMap = new Gson().fromJson(s, new TypeToken<TreeMap<String, Object>>() {
        }.getType());

        return new Gson().toJson(treeMap);
    }

}
