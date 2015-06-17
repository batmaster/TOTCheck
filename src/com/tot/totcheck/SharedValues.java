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
	public static final String HOST_VERSION = "http://203.114.104.242/umbo/getVersion.php";
	
	public final static String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/totapk/";
	public final static String FILE_NAME = "TOTUmbo.apk";
	public static final String HOST_APK = "http://203.114.104.242/umbo/TOTUmbo.apk";
	
	public static final String TOT_PREF_SETTINGS = "TOT_PREF_SETTINGS";
	public static final String TOT_PREF_PROVINCES = "TOT_PREF_PROVINCES";
	
	public static final String TOT_SQLITE_DB = "umbo.db";
	public static final String TOT_SQLITE_UP_LISTENING_TABLE = "upListening";
	
	private SharedValues () {
		
	}
	
	public static void setEnableStatePref(Context context, String prefName, String key, boolean enable) {
		SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, enable);
		editor.commit();
	}
	
	public static boolean getEnableStatePref(Context context, String prefName, String key) {
		SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}
	
	public static boolean hasEnableStatePref(Context context, String prefName, String key) {
		SharedPreferences sp = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		return sp.contains(key);
	}
	
	public static String[] getEnableProvinces(Context context) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_PROVINCES, Context.MODE_PRIVATE);
		HashMap<String, Boolean> map = (HashMap<String, Boolean>) sp.getAll();
		ArrayList<String> list = new ArrayList<String>();
		
		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry e = (Map.Entry) iter.next();
			if (((Boolean) e.getValue()) == true) {
			    list.add((String) e.getKey());
			}
		}
		
		String[] provinces = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			provinces[i] = list.get(i);
		}
		
		return provinces;
	}
	
	public static int getLastestNotifiedId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_SETTINGS, Context.MODE_PRIVATE);
		return sp.getInt("lastestId", -1);
	}
	
	public static void setLastestNotifiedId(Context context, int id) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("lastestId", id);
		editor.commit();
	}
	
	public static String getLastUsedProvince(Context context) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_SETTINGS, Context.MODE_PRIVATE);
		return sp.getString("lastUsedProvince", "");
	}
	
	public static void setLastUsedProvince(Context context, String province) {
		SharedPreferences sp = context.getSharedPreferences(TOT_PREF_SETTINGS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("lastUsedProvince", province);
		editor.commit();
	}
	
	public static ArrayList<Integer> getUpListeningIds(Context context) {
		return DBHelper.getInstance(context).getUpListeningIds();
	}
	
	public static void addUpListeningId(Context context, ArrayList<Integer> list) {
		DBHelper.getInstance(context).addUpListeningId(context, list);
	}
	
	public static void removeUpListeningIds(Context context, ArrayList<Integer> list) {
		DBHelper.getInstance(context).removeUpListeningIds(context, list);
	}
	
}

class DBHelper extends SQLiteOpenHelper {
	
	private String CREATE_TABLE = String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT, upListeningId INTEGER)", SharedValues.TOT_SQLITE_UP_LISTENING_TABLE);
	
	private static DBHelper instance;

    private DBHelper(Context context) {
        super(context, SharedValues.TOT_SQLITE_DB, null, 1);
    }
    
    public static DBHelper getInstance(Context context) {
    	if (instance == null)
    		instance = new DBHelper(context);
    	return instance;
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public ArrayList<Integer> getUpListeningIds() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		SQLiteDatabase db = getReadableDatabase();

		String sql = String.format("SELECT * FROM %s", SharedValues.TOT_SQLITE_UP_LISTENING_TABLE);
		Log.d("sql", sql);
	    Cursor cursor = db.rawQuery(sql, null);

	    if (cursor != null) {
	        cursor.moveToFirst();
	    }

	    while (!cursor.isAfterLast()) {
	    	list.add(cursor.getInt(1));
	        cursor.moveToNext();
	    }

		return list;
	}
	
	public void addUpListeningId(Context context, ArrayList<Integer> list) {
		if (list.size() == 0)
			return;
		
		SQLiteDatabase db = getWritableDatabase();

		String ids = "";
		for (int i = 0; i < list.size(); i++) {
			ids += "(" + list.get(i) + ")";
			
			if (i != list.size() - 1)
				ids += ",";
		}
		
		String sql = String.format("INSERT INTO %s (upListeningId) VALUES %s", SharedValues.TOT_SQLITE_UP_LISTENING_TABLE, ids);
		Log.d("sql", sql);
	    db.execSQL(sql);
	}
	
	public void removeUpListeningIds(Context context, ArrayList<Integer> list) {
		if (list.size() == 0)
			return;
		
		SQLiteDatabase db = getWritableDatabase();

		String ids = "(";
		for (int i = 0; i < list.size(); i++) {
			ids += list.get(i);
			
			if (i != list.size() - 1)
				ids += ",";
		}
		ids += ")";
		
		String sql = String.format("DELETE FROM %s WHERE upListeningId IN %s", SharedValues.TOT_SQLITE_UP_LISTENING_TABLE, ids);
		Log.d("sql", sql);
	    db.execSQL(sql);
	}
}
