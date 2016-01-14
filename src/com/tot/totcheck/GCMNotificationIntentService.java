package com.tot.totcheck;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
 
public class GCMNotificationIntentService extends GcmListenerService {

	private static final int NOTIFICATION_DOWN_CODE = 65535;
	private static final int NOTIFICATION_UP_CODE = 65536;
	
	private static NotificationManager notificationManager;
	private static PendingIntent pIntent;
	
    @Override
    public void onMessageReceived(String from, Bundle message) {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        
//        if (SharedValues.getEnableStatePref(getApplicationContext(), SharedValues.TOT_PREF_SETTINGS, "notification")) {
			if (notificationManager == null)
				notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			
			if (pIntent == null) {
				Intent appIntent = new Intent(getApplicationContext(), MainFragmentActivity.class);
				appIntent.putExtra("notificationTab", true);
				appIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				pIntent = PendingIntent.getActivity(getApplicationContext(), 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			}
 
	        if (!message.isEmpty()) {
	        	
	        	try {
	        		int type = Integer.parseInt(message.getString("title"));
					String parsed = Parser.parse(message.getString("body"));
					
					JSONArray js = new JSONArray(parsed);
					for (int i = 0; i < js.length(); i++) {
						JSONObject jo = js.getJSONObject(i);
						ListViewRowItem item = new ListViewRowItem(jo.getString("id_nu"), jo.getString("node_id"), jo.getString("node_ip"), jo.getString("node_time_down"), jo.getString("node_name"), jo.getString("temp"), jo.getString("province"));
						
						Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						Log.d("gcm", item.getNode_name());
				        NotificationCompat.Builder noti = new NotificationCompat.Builder(getApplicationContext());
						RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
						remoteViews.setTextViewText(R.id.textViewDevice, item.getProvince() + " " + item.getNode_name());
						remoteViews.setTextColor(R.id.textViewDevice, Color.parseColor("#4B4B4B"));
						remoteViews.setTextViewText(R.id.textViewStatus, type == 0 ? "DOWN " : "UP ");
						remoteViews.setTextColor(R.id.textViewStatus, type == 0 ? Color.parseColor("#FF2222") : Color.parseColor("#22FF22"));
						remoteViews.setTextViewText(R.id.textViewTime, type == 0 ? item.getNode_time_down() : "");
						remoteViews.setImageViewResource(R.id.imageViewIcon, R.drawable.ic_launcher);
						noti.setContent(remoteViews);
				        noti.setContentIntent(pIntent);
				        noti.setSound(soundUri);
				        noti.setAutoCancel(true);
				        
				        // dummy
				        noti.setContentText("Title");
				        noti.setContentText("Text");
				        noti.setSmallIcon(R.drawable.ic_launcher);
				        
						notificationManager.notify(type == 0 ? NOTIFICATION_DOWN_CODE : NOTIFICATION_UP_CODE, noti.build());
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
//	        }
        	
        }
    }
}