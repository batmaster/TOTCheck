package com.tot.totcheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class SharedValues {
	
	// http://128.199.145.53/tot/
	public static final String HOST_DB = "http://203.114.104.242/umbo/getRecord.php";
//	public static final String HOST_DB = "http://192.168.20.123:8888/reg.php";
	public static final String HOST_VERSION = "http://203.114.104.242/umbo/getVersion.php";
	
	public final static String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/totapk/";
	public final static String FILE_NAME = "TOTUmbo.apk";
	public static final String HOST_APK = "http://203.114.104.242/umbo/TOTUmbo.apk";
	
	public static final String TOT_PREF_SETTINGS = "TOT_PREF_SETTINGS";
	public static final String TOT_PREF_PROVINCES = "TOT_PREF_PROVINCES";
	
	public static final String KEY_REGID = "regId";
	public static final String KEY_NOTIFICATION = "notification";
	public static final String KEY_LAST_USED_PROVINCE = "last_province";
	
	public static final String TOT_SQLITE_DB = "umbo.db";
	public static final String TOT_SQLITE_UP_LISTENING_TABLE = "upListening";
	
	public static final String SENDER_ID = "748562068269";
	
	private SharedValues () {
		
	}
	
	public static void setBooleanPref(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static boolean getBooleanPref(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_SETTINGS, Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}
	
	public static void setStringPref(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getStringPref(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_SETTINGS, Context.MODE_PRIVATE);
		return sp.getString(key, null);
	}

	public static boolean getStateProvince(Context context, String province) {
		return getBooleanPref(context, province);
	}
	
}
