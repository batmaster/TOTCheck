package com.tot.totcheck;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class NotificationService extends Service {
	
	private static BroadcastReceiver notificationReceiver;
	private final static IntentFilter intentTimeTickFilter;
	
	static {
		intentTimeTickFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		notificationReceiver = new NotificationReceiver();
		registerReceiver(notificationReceiver, intentTimeTickFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(notificationReceiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


}
