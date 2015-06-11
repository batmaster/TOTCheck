package com.tot.totcheck;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
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
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent notificatonService = new Intent(context, NotificationService.class);
			context.startService(notificatonService);
		}
		else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
//			SharedValues.setLastestNotifiedId(context, 0);
			if (SharedValues.getEnableStatePref(context, SharedValues.TOT_PREF_SETTINGS, "notification")) {
				if (notificationManager == null)
					notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				
				if (pIntent == null) {
					Intent appIntent = new Intent(context, MainFragmentActivity.class);
					appIntent.putExtra("notificationTab", true);
					pIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
				}
				
				GetListTask job = new GetListTask(context);
				job.execute();
			}
		}
	}
	
	private class GetListTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private List<ListViewRowItem> list;
		
		public GetListTask(Context context) {
			this.context = context;
		}
		
		@Override
		protected String doInBackground(String[] params) {
			list = new ArrayList<ListViewRowItem>();
			
			try {
				String parsed = Parser.parse(Request.requestList(SharedValues.getEnableProvinces(context)));
				JSONArray js = new JSONArray(parsed);
				for (int i = 0; i < js.length(); i++) {
					JSONObject jo = js.getJSONObject(i);
					ListViewRowItem item = new ListViewRowItem(jo.getString("id_nu"), jo.getString("node_id"), jo.getString("node_ip"), jo.getString("node_time_down"), jo.getString("smsdown"), jo.getString("smsup"), jo.getString("node_name"), jo.getString("province"));
					list.add(item);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				list.add(new ListViewRowItem());
			}
			
			if (list.size() == 0)
				list.add(new ListViewRowItem());
			
			return "some message";
		}

		@Override
		protected void onPostExecute(String message) {
			
			for (int i = 0; i < list.size(); i++) {
				if (Integer.parseInt(list.get(i).getId_nu()) > SharedValues.getLastestNotifiedId(context)) {

					Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					
					NotificationCompat.Builder noti = new NotificationCompat.Builder(context);
			        noti.setContentTitle(list.get(i).getProvince() + " " + list.get(i).getNode_name());
			        noti.setContentText(list.get(i).getNode_time_down());
			        noti.setSmallIcon(R.drawable.ic_launcher);
			        noti.setContentIntent(pIntent);
			        noti.setSound(soundUri);
					notificationManager.notify(NOTIFICATION_CODE, noti.build());
					
					SharedValues.setLastestNotifiedId(context, Integer.parseInt(list.get(i).getId_nu()));
				}
			}
		}
		
		public void execute() {
			execute("test");
		}
	}
}
