package com.example.amankassahun;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Aman on 5/8/2018.
 */

public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";
    private static final String PREF_AUTHORITY = "authority_preference";
    private static final String LATEST_POST_MAKERSKEY="latestmakersPost";
    private static final String LATEST_POST_ACCKEY="latestaccPost";
    private static final String LATEST_POST_SOLVEKEY="latestsolvePost";
    private static final String LATEST_POST_DIEKEY="latestdiePost";
    private static final String LATEST_POST_ICOGKEY="latesticogPost";
    private static final String PREF_WHICH_PARTITION = "which_partition";
    private static final String BEFOREHIRUY="before_hiruy";
    private static final String HIRUY ="hiruy";
    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }
    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }
    public static String getLastResultId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID, null);
    }
    public static void setLatestPostmakerskey(Context context, String latestKey) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LATEST_POST_MAKERSKEY, latestKey)
                .apply();
    }
    public static String getLatestPostmakersKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LATEST_POST_MAKERSKEY, "12");
    }
    public static void setLatestPostacckey(Context context, String latestKey) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LATEST_POST_ACCKEY, latestKey)
                .apply();
    }
    public static String getLatestPostaccKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LATEST_POST_ACCKEY, "12");
    }
    public static void setLatestPostsolveKey(Context context, String latestKey) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LATEST_POST_SOLVEKEY, latestKey)
                .apply();
    }
    public static String getLatestPostsolveKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LATEST_POST_SOLVEKEY, "12");
    }
    public static void setLatestPostdiekey(Context context, String latestKey) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LATEST_POST_DIEKEY, latestKey)
                .apply();
    }
    public static String getLatestPostdieKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LATEST_POST_DIEKEY, "12");
    }
    public static void setLatestPosticogKey(Context context, String latestKey) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(LATEST_POST_ICOGKEY, latestKey)
                .apply();
    }
    public static String getLatestPosticogKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(LATEST_POST_ICOGKEY, "12");
    }
    public static void setLastResultId(Context context, String lastResultId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID, lastResultId)
                .apply();
    }
    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }
    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();
    }
    public static Set<String> getAuthority2(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(BEFOREHIRUY,null);
    }
    public static Set<String> getAuthority(Context context) {
        Set<String> set= new HashSet<>();
        set.add("x");
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(PREF_AUTHORITY,set);
    }
    public static void setAuthority(Context context, Set<String> isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(PREF_AUTHORITY, isOn)
                .apply();
    }

    public static String getDept(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_WHICH_PARTITION,"public");
    }

    public static void setDept(Context context, String dept) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_WHICH_PARTITION , dept)
                .apply();
    }
    public static boolean getHiruy(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(HIRUY,true);
    }
    public static void setHiruy(Context context, boolean dept) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(HIRUY , dept)
                .apply();
    }


}
