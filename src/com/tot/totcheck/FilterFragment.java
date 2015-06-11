package com.tot.totcheck;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class FilterFragment extends Fragment {
	
	private Spinner spinnerProvinces;
	private ListView listView;
	
	public FilterFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_filter, container, false);
		
		spinnerProvinces = (Spinner) view.findViewById(R.id.spinnerProvinces);
		GetProvincesTask getProvinces = new GetProvincesTask();
		getProvinces.execute();
		spinnerProvinces.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				GetListTask job = new GetListTask(spinnerProvinces.getSelectedItem().toString());
				job.execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		listView = (ListView) view.findViewById(R.id.listView);
		
		return view;
	}
	
	private class GetProvincesTask extends AsyncTask<String, Integer, String> {

		private String[] provinces;
		private int[] amounts;
		
		private ProgressDialog loading;
		
		public GetProvincesTask() {
			loading = new ProgressDialog(getActivity());
			loading.setTitle("รายชื่อจังหวัด");
			loading.setMessage("กำลังโหลด...");
			loading.setCancelable(false);
			loading.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		}
		
		@Override
		protected String doInBackground(String[] params) {
			try {
				String parsed = Parser.parse(Request.request(Request.REQ_GET_PROVINCES));
				JSONArray js = new JSONArray(parsed);
				provinces = new String[js.length()];
				amounts = new int[js.length()];
				
				for (int i = 0; i < js.length(); i++) {
					JSONObject jo = js.getJSONObject(i);
					provinces[i] = jo.getString("province");
					amounts[i] = jo.getInt("amount");
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
			loading.dismiss();
			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_row, R.id.textView, provinces);
			spinnerProvinces.setAdapter(spinnerAdapter);
		}
		
		public void execute() {
			execute("test");
		}
	}
	
	private class GetListTask extends AsyncTask<String, Integer, String> {

		private String province;
		private List<ListViewRowItem> list;
		
		private ProgressDialog loading;
		
		public GetListTask(String province) {
			this.province = province;
			loading = new ProgressDialog(getActivity());
			loading.setTitle("รายการอุปกรณ์");
			loading.setMessage("กำลังโหลด...");
			loading.setCancelable(false);
			loading.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		}
		
		@Override
		protected String doInBackground(String[] params) {
			list = new ArrayList<ListViewRowItem>();
			
			try {
				String parsed = Parser.parse(Request.requestList(province));
				JSONArray js = new JSONArray(parsed);
				for (int i = 0; i < js.length(); i++) {
					JSONObject jo = js.getJSONObject(i);
					ListViewRowItem item = new ListViewRowItem(jo.getString("id_nu"), jo.getString("node_id"), jo.getString("node_ip"), jo.getString("node_time_down"), jo.getString("smsdown"), jo.getString("smsup"), jo.getString("node_name"));
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
		protected void onPreExecute() {
			loading.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String message) {
			loading.dismiss();
			listView.setAdapter(null);
			ListViewRowAdapter adapter = new ListViewRowAdapter(getActivity().getApplicationContext(), list);
			listView.setAdapter(adapter);
		}
		
		public void execute() {
			execute("test");
		}
	}
}
