package com.tot.totcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedValues {
	
	public static final String TOT_PREF_SETTINGS = "TOT_PREF_SETTINGS";
	public static final String TOT_PREF_PROVINCES = "TOT_PREF_PROVINCES";
	
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
}
