package com.flyfish.myapplication;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import java.net.URLEncoder;
import java.util.Random;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 *
 * Created on 2020-05-19
 */
public class SearchInfo {


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
    private String sid = "1a8a754630af722fed71cdcc692de39d";
    private String uid = "2200613420057";
    @SerializedName("x-features")
    private String xfeatures = "27";
    private String appKey = "21407387";
    private String api = "mtop.taobao.idle.search.glue";
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
        deviceId = getRandomString("", 44);
        //        deviceId = "AqUio3vapJwwKQxbpIirTxX64VwPDbFlLHygZBfEEhCa";
        utdid = callMethod(Search.did, "getValueForUpdate") + "";
        sid = "";
        uid = "";
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SearchInfo(String keyword) {
        SearchItem searchItem = new SearchItem();
        searchItem.setKeyword(keyword);
        data = searchItem.toString();
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

class SearchItem {

    /**
     * activeSearch : true
     * bizFrom : home
     * channelFrontFilterNavEnable : true
     * devicePixelRatio : 2.625
     * forceUseInputKeyword : false
     * forceUseTppRepair : false
     * fromCombo : Sort
     * fromKits : false
     * fromLeaf : false
     * fromShade : false
     * fromSuggest : false
     * gps : 40.078861,116.290125
     * keyword : 苹果
     * latitude : 40.078861
     * longitude : 116.290125
     * pageNumber : 1
     * resultListLastIndex : 12
     * rowsPerPage : 10
     * screenHeight : 683.4285714285714
     * screenWidth : 411.42857142857144
     * searchReqFromPage : xyHome
     * shadeBucketNum : -1
     * sortField : time
     * sortValue : desc
     * suggestBucketNum : 31
     */

    private boolean activeSearch = true;
    private String bizFrom = "home";
    private boolean channelFrontFilterNavEnable = true;
    private double devicePixelRatio = 2.625;
    private boolean forceUseInputKeyword = false;
    private boolean forceUseTppRepair = false;
    private String fromCombo = "Sort";
    private boolean fromKits = false;
    private boolean fromLeaf = false;
    private boolean fromShade = false;
    private boolean fromSuggest = false;
    private String gps = "40.078861,116.290125";
    private String keyword;
    private String latitude = "40.078861";
    private String longitude = "116.290125";
    private Integer pageNumber = 1;
    private Integer resultListLastIndex = 12;
    private Integer rowsPerPage = 10;
    private double screenHeight = 683.4285714285714;
    private double screenWidth = 411.42857142857144;
    private String searchReqFromPage = "xyHome";
    private Integer shadeBucketNum = -1;
    private String sortField = "time";
    private String sortValue = "desc";
    private Integer suggestBucketNum = 31;

    public boolean isActiveSearch() {
        return activeSearch;
    }

    public void setActiveSearch(boolean activeSearch) {
        this.activeSearch = activeSearch;
    }

    public String getBizFrom() {
        return bizFrom;
    }

    public void setBizFrom(String bizFrom) {
        this.bizFrom = bizFrom;
    }

    public boolean isChannelFrontFilterNavEnable() {
        return channelFrontFilterNavEnable;
    }

    public void setChannelFrontFilterNavEnable(boolean channelFrontFilterNavEnable) {
        this.channelFrontFilterNavEnable = channelFrontFilterNavEnable;
    }

    public double getDevicePixelRatio() {
        return devicePixelRatio;
    }

    public void setDevicePixelRatio(double devicePixelRatio) {
        this.devicePixelRatio = devicePixelRatio;
    }

    public boolean isForceUseInputKeyword() {
        return forceUseInputKeyword;
    }

    public void setForceUseInputKeyword(boolean forceUseInputKeyword) {
        this.forceUseInputKeyword = forceUseInputKeyword;
    }

    public boolean isForceUseTppRepair() {
        return forceUseTppRepair;
    }

    public void setForceUseTppRepair(boolean forceUseTppRepair) {
        this.forceUseTppRepair = forceUseTppRepair;
    }

    public String getFromCombo() {
        return fromCombo;
    }

    public void setFromCombo(String fromCombo) {
        this.fromCombo = fromCombo;
    }

    public boolean isFromKits() {
        return fromKits;
    }

    public void setFromKits(boolean fromKits) {
        this.fromKits = fromKits;
    }

    public boolean isFromLeaf() {
        return fromLeaf;
    }

    public void setFromLeaf(boolean fromLeaf) {
        this.fromLeaf = fromLeaf;
    }

    public boolean isFromShade() {
        return fromShade;
    }

    public void setFromShade(boolean fromShade) {
        this.fromShade = fromShade;
    }

    public boolean isFromSuggest() {
        return fromSuggest;
    }

    public void setFromSuggest(boolean fromSuggest) {
        this.fromSuggest = fromSuggest;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getResultListLastIndex() {
        return resultListLastIndex;
    }

    public void setResultListLastIndex(int resultListLastIndex) {
        this.resultListLastIndex = resultListLastIndex;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(double screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(double screenWidth) {
        this.screenWidth = screenWidth;
    }

    public String getSearchReqFromPage() {
        return searchReqFromPage;
    }

    public void setSearchReqFromPage(String searchReqFromPage) {
        this.searchReqFromPage = searchReqFromPage;
    }

    public int getShadeBucketNum() {
        return shadeBucketNum;
    }

    public void setShadeBucketNum(int shadeBucketNum) {
        this.shadeBucketNum = shadeBucketNum;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

    public int getSuggestBucketNum() {
        return suggestBucketNum;
    }

    public void setSuggestBucketNum(int suggestBucketNum) {
        this.suggestBucketNum = suggestBucketNum;
    }

    @Override
    public String toString() {

        String s = new Gson().toJson(this);
        TreeMap<String, Object> treeMap = new Gson().fromJson(s, new TypeToken<TreeMap<String, Object>>() {
        }.getType());
        treeMap.put("pageNumber", getPageNumber());
        treeMap.put("resultListLastIndex", getResultListLastIndex());
        treeMap.put("rowsPerPage", getRowsPerPage());
        treeMap.put("shadeBucketNum", getShadeBucketNum());
        treeMap.put("suggestBucketNum", getSuggestBucketNum());
        return new Gson().toJson(treeMap);
    }

    public static void main(String[] args) {
        SearchItem searchItem = new SearchItem();
        searchItem.setKeyword("asdsad");

        System.out.println(searchItem);
    }
}
