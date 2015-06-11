package com.tot.totcheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SettingFragment extends Fragment {
	
	private PreferenceBoxView preferenceBoxViewNotification;
	private LinearLayout linearLayoutSetting;
	
	public SettingFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		
		preferenceBoxViewNotification = (PreferenceBoxView) view.findViewById(R.id.preferenceBoxViewNotification);
		preferenceBoxViewNotification.setChecked(SharedValues.getEnableStatePref(getActivity().getApplicationContext(), SharedValues.TOT_PREF_SETTINGS, "notification"));
		
		linearLayoutSetting = (LinearLayout) view.findViewById(R.id.linearLayoutSetting);
		GetProvincesTask getProvinces = new GetProvincesTask();
		getProvinces.execute();
		return view;
	}
	
	private class GetProvincesTask extends AsyncTask<String, Integer, String> {
		
		private Context context;
		private ProgressDialog loading;
		
		private String[] provinces;
		
		public GetProvincesTask() {
			loading = new ProgressDialog(getActivity());
			loading.setTitle("รายชื่อจังหวัด");
			loading.setMessage("กำลังโหลด...");
			loading.setCancelable(false);
			loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			
			context = getActivity().getApplicationContext();
		}
		
		@Override
		protected String doInBackground(String[] params) {
			try {
				String parsed = Parser.parse(Request.request(Request.REQ_GET_PROVINCES));
				JSONArray js = new JSONArray(parsed);
				provinces = new String[js.length()];
				for (int i = 0; i < js.length(); i++) {
					JSONObject jo = js.getJSONObject(i);
					provinces[i] = jo.getString("province");
					boolean has = SharedValues.hasEnableStatePref(context, SharedValues.TOT_PREF_PROVINCES, provinces[i]);
					if (!has) {
						SharedValues.setEnableStatePref(context, SharedValues.TOT_PREF_PROVINCES, provinces[i], false);
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return "some message";
		}

		@Override
		protected void onPreExecute() {
			loading.show();
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(String message) {
			for (int i = 0; i < provinces.length; i++) {
				PreferenceBoxView box = new PreferenceBoxView(context);
				box.setTitle(provinces[i]);
				box.setKey(provinces[i]);
				box.setChecked(SharedValues.getEnableStatePref(context, SharedValues.TOT_PREF_PROVINCES, provinces[i]));
				linearLayoutSetting.addView(box);
			}
			
			loading.dismiss();
		}
		
		public void execute() {
			execute("test");
		}
	}

}
