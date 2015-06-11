package com.tot.totcheck;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class NotificationFragment extends Fragment {
	
	private ListView listView;
	
	public NotificationFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notification, container, false);
		listView = (ListView) view.findViewById(R.id.listView);
		
		GetListTask job = new GetListTask(getActivity().getApplicationContext());
		job.execute();
		
		return view;
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
			
			listView.setAdapter(null);
			ListViewRowAdapter adapter = new ListViewRowAdapter(getActivity().getApplicationContext(), list, true);
			listView.setAdapter(adapter);
		}
		
		public void execute() {
			execute("test");
		}
	}
}
