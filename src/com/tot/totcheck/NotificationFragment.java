package com.tot.totcheck;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class NotificationFragment extends Fragment {
	
	private SwipeRefreshLayout swipeRefreshLayout;
	
	private ListView listView;
	
	public NotificationFragment() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notification, container, false);
		listView = (ListView) view.findViewById(R.id.listView);
		
		GetListTask job = new GetListTask(getActivity().getApplicationContext());
		job.execute();
		
		swipeRefreshLayout = new SwipeRefreshLayout(getActivity().getApplicationContext());
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				GetListTask job = new GetListTask(getActivity().getApplicationContext());
				job.execute();
				
				swipeRefreshLayout.setRefreshing(false);
			}
		});
		swipeRefreshLayout.addView(view);
		return swipeRefreshLayout;
	}
	
	private class GetListTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private List<ListViewRowItem> list;
		
		private ProgressDialog loading;
		
		public GetListTask(Context context) {
			this.context = context;
			
			loading = new ProgressDialog(getActivity());
			loading.setTitle("รายการแจ้งเตือน");
			loading.setMessage("กำลังโหลด...");
//			loading.setCancelable(false);
			loading.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		}
		
		@Override
		protected String doInBackground(String[] params) {
			list = new ArrayList<ListViewRowItem>();
			
			try {
				String parsed = Parser.parse(Request.requestDownList(SharedValues.getEnableProvinces(context)));
				JSONArray js = new JSONArray(parsed);
				for (int i = 0; i < js.length(); i++) {
					JSONObject jo = js.getJSONObject(i);
					ListViewRowItem item = new ListViewRowItem(jo.getString("id_nu"), jo.getString("node_id"), jo.getString("node_ip"), jo.getString("node_time_down"), jo.getString("smsdown"), jo.getString("smsup"), jo.getString("node_name"), jo.getString("temp"), jo.getString("province"));
					list.add(item);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				list.add(new ListViewRowItem());
			} catch (ConnectTimeoutException e) {
				loading.setMessage("เชื่อมต่อเซิร์ฟนานเกินไป");
			} catch (SocketTimeoutException e) {
				loading.setMessage("รอผลตอบกลับนานเกินไป");
			} catch (HttpHostConnectException e) {
				loading.setMessage("เชื่อมต่อเซิร์ฟเวอร์ไม่ได้");
				e.printStackTrace();
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
			ListViewRowAdapter adapter = new ListViewRowAdapter(getActivity().getApplicationContext(), list, true);
			listView.setAdapter(adapter);
		}
		
		public void execute() {
			execute("test");
		}
	}
}
