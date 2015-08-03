package com.tot.totcheck;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
	
	private static final int NOTIFICATION_DOWN_CODE = 65535;
	private static final int NOTIFICATION_UP_CODE = 65536;
	
	private static NotificationManager notificationManager;
	private static PendingIntent pIntent;
	
	@Override
	public void onReceive(Context context, Intent intent) {
//		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//		v.vibrate(400);
//		Toast.makeText(context, "run", Toast.LENGTH_SHORT).show();
		
//		SharedValues.setLastestNotifiedId(context, 0);
		if (SharedValues.getEnableStatePref(context, SharedValues.TOT_PREF_SETTINGS, "notification")) {
			if (notificationManager == null)
				notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			if (pIntent == null) {
				Intent appIntent = new Intent(context, MainFragmentActivity.class);
				appIntent.putExtra("notificationTab", true);
				appIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				pIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			}
			
			GetDownTask jobDown = new GetDownTask(context);
			jobDown.execute();
			
			GetUpTask jobUp = new GetUpTask(context);
			jobUp.execute();
		}
	}
	
	private class GetDownTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private List<ListViewRowItem> downList;
		
		public GetDownTask(Context context) {
			this.context = context;
		}
		
		@Override
		protected String doInBackground(String[] params) {
			downList = new ArrayList<ListViewRowItem>();
			
			try {
				String parsed = Parser.parse(Request.requestDownList(SharedValues.getEnableProvinces(context)));
				JSONArray js = new JSONArray(parsed);
				for (int i = 0; i < js.length(); i++) {
					JSONObject jo = js.getJSONObject(i);
					ListViewRowItem item = new ListViewRowItem(jo.getString("id_nu"), jo.getString("node_id"), jo.getString("node_ip"), jo.getString("node_time_down"), jo.getString("smsdown"), jo.getString("smsup"), jo.getString("node_name"), jo.getString("temp"), jo.getString("province"));
					downList.add(item);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				downList.add(new ListViewRowItem());
			} catch (ConnectTimeoutException e) {
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			} catch (HttpHostConnectException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (downList.size() == 0)
				downList.add(new ListViewRowItem());
			
			return "some message";
		}

		@Override	
		protected void onPostExecute(String message) {
			ArrayList<Integer> upListeningIdList = new ArrayList<Integer>();
			for (int i = 0; i < downList.size(); i++) {
				if (downList.get(i).getId_nu() != "" && Integer.parseInt(downList.get(i).getId_nu()) > SharedValues.getLastestNotifiedId(context)) {

					Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					
			        NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
					RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
					remoteViews.setTextViewText(R.id.textViewDevice, downList.get(i).getProvince() + " " + downList.get(i).getNode_name());
					remoteViews.setTextColor(R.id.textViewDevice, Color.parseColor("#4B4B4B"));
					remoteViews.setTextViewText(R.id.textViewStatus, "DOWN ");
					remoteViews.setTextColor(R.id.textViewStatus, Color.parseColor("#FF2222"));
					remoteViews.setTextViewText(R.id.textViewTime, downList.get(i).getNode_time_down());
					remoteViews.setImageViewResource(R.id.imageViewIcon, R.drawable.ic_launcher);
					noti.setContent(remoteViews);
			        noti.setContentIntent(pIntent);
			        noti.setSound(soundUri);
			        noti.setAutoCancel(true);
			        
			        // dummy
			        noti.setContentText("Title");
			        noti.setContentText("Text");
			        noti.setSmallIcon(R.drawable.ic_launcher);
			        
					notificationManager.notify(NOTIFICATION_DOWN_CODE, noti.build());
					
					SharedValues.setLastestNotifiedId(context, Integer.parseInt(downList.get(i).getId_nu()));
					upListeningIdList.add(Integer.parseInt(downList.get(i).getId_nu()));
				}
			}

			SharedValues.addUpListeningId(context, upListeningIdList);
		}
		
		public void execute() {
			execute("test");
		}
	}
	
	private class GetUpTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private List<ListViewRowItem> upList;
		
		public GetUpTask(Context context) {
			this.context = context;
		}
		
		@Override
		protected String doInBackground(String[] params) {
			upList = new ArrayList<ListViewRowItem>();
			
			try {
				String parsed = Parser.parse(Request.requestUpList(SharedValues.getUpListeningIds(context)));
				JSONArray js = new JSONArray(parsed);
				for (int i = 0; i < js.length(); i++) {
					JSONObject jo = js.getJSONObject(i);
					ListViewRowItem item = new ListViewRowItem(jo.getString("id_nu"), jo.getString("node_id"), jo.getString("node_ip"), jo.getString("node_time_down"), jo.getString("smsdown"), jo.getString("smsup"), jo.getString("node_name"), jo.getString("temp"), jo.getString("province"));
					upList.add(item);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				upList.add(new ListViewRowItem());
			} catch (ConnectTimeoutException e) {
				
			} catch (SocketTimeoutException e) {
				
			} catch (HttpHostConnectException e) {
				
			} catch (IOException e) {
				
			}
			
			if (upList.size() == 0)
				upList.add(new ListViewRowItem());
			
			return "some message";
		}

		@Override	
		protected void onPostExecute(String message) {
			ArrayList<Integer> uppedList = new ArrayList<Integer>();
			for (int i = 0; i < upList.size(); i++) {
				if (upList.get(i).getId_nu() != "") {

					Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					
					NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
					RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
					remoteViews.setTextViewText(R.id.textViewDevice, upList.get(i).getProvince() + " " + upList.get(i).getNode_name());
					remoteViews.setTextColor(R.id.textViewDevice, Color.parseColor("#4B4B4B"));
					remoteViews.setTextViewText(R.id.textViewStatus, "UP");
					remoteViews.setTextColor(R.id.textViewStatus, Color.parseColor("#22FF22"));
					remoteViews.setTextViewText(R.id.textViewTime, "");
					remoteViews.setImageViewResource(R.id.imageViewIcon, R.drawable.ic_launcher);
					noti.setContent(remoteViews);
			        noti.setContentIntent(pIntent);
			        noti.setSound(soundUri);
			        noti.setAutoCancel(true);
			        
			        // dummy
			        noti.setContentText("Title");
			        noti.setContentText("Text");
			        noti.setSmallIcon(R.drawable.ic_launcher);
			        
					notificationManager.notify(NOTIFICATION_UP_CODE, noti.build());
					
					uppedList.add(Integer.parseInt(upList.get(i).getId_nu()));
				}
			}

			SharedValues.removeUpListeningIds(context, uppedList);
		}
		
		public void execute() {
			execute("test");
		}
	}

}
