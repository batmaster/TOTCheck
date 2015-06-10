package com.tot.totcheck;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
	
	private static final int NOTIFICATION_CODE = 65535;
	
	private static NotificationManager notificationManager;
	private static PendingIntent pIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Toast.makeText(context, "From tot: " + intent.getAction(), Toast.LENGTH_SHORT).show();
		
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent notificatonService = new Intent(context, NotificationService.class);
			context.startService(notificatonService);
		}
		else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
			if (notificationManager == null)
				notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			if (pIntent == null)
				pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainFragmentActivity.class), 0);
			
			NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
	        noti.setContentTitle("Title");
	        noti.setContentText("text : " + Calendar.getInstance().getTime());
	        noti.setSmallIcon(R.drawable.ic_launcher);
	        noti.setContentIntent(pIntent);
			notificationManager.notify(NOTIFICATION_CODE, noti.build()); 
		}
	}

}
