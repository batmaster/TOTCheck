package com.tot.totcheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String[] PROVINCES = {"กรุงเทพมหานคร", "กระบี่", "กาญจนบุรี", "กาฬสินธุ์", "กำแพงเพชร", "ขอนแก่น", "จันทบุรี", "ฉะเชิงเทรา", "ชลบุรี", "ชัยนาท", "ชัยภูมิ", "ชุมพร", "เชียงราย", "เชียงใหม่", "ตรัง", "ตราด", "ตาก", "นครนายก", "นครปฐม", "นครพนม", "นครราชสีมา", "นครศรีธรรมราช", "นครสวรรค์", "นนทบุรี", "นราธิวาส", "น่าน", "บึงกาฬ", "บุรีรัมย์", "ปทุมธานี", "ประจวบคีรีขันธ์", "ปราจีนบุรี", "ปัตตานี", "พระนครศรีอยุธยา", "พังงา", "พัทลุง", "พิจิตร", "พิษณุโลก", "เพชรบุรี", "เพชรบูรณ์", "แพร่", "พะเยา", "ภูเก็ต", "มหาสารคาม", "มุกดาหาร", "แม่ฮ่องสอน", "ยะลา", "ยโสธร", "ร้อยเอ็ด", "ระนอง", "ระยอง", "ราชบุรี", "ลพบุรี", "ลำปาง", "ลำพูน", "เลย", "ศรีสะเกษ", "สกลนคร", "สงขลา", "สตูล", "สมุทรปราการ", "สมุทรสงคราม", "สมุทรสาคร", "สระแก้ว", "สระบุรี", "สิงห์บุรี", "สุโขทัย", "สุพรรณบุรี", "สุราษฎร์ธานี", "สุรินทร์", "หนองคาย", "หนองบัวลำภู", "อ่างทอง", "อุดรธานี", "อุทัยธานี", "อุตรดิตถ์", "อุบลราชธานี", "อำนาจเจริญ"};
	private Spinner spinnerProvinces;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		spinnerProvinces = (Spinner) findViewById(R.id.spinnerProvinces);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_row, R.id.textView, PROVINCES);
		spinnerProvinces.setAdapter(adapter);
		spinnerProvinces.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				SendFeedbackJob job = new SendFeedbackJob(spinnerProvinces.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		listView = (ListView) findViewById(R.id.listView);
	}
	
	private class SendFeedbackJob extends AsyncTask<String, Void, String> {

		private String province;
		private List<ListViewRowItem> list;
		
		public SendFeedbackJob(String province) {
			this.province = province;
			execute("test");
		}
		
		@Override
		protected String doInBackground(String[] params) {
			list = requestServer(province);
			return "some message";
		}

		@Override
		protected void onPostExecute(String message) {
			listView.setAdapter(null);
			ListViewRowAdapter adapter = new ListViewRowAdapter(getApplicationContext(), list);
			listView.setAdapter(adapter);
		}
	}
	
	private List<ListViewRowItem> requestServer(String province) {
		List<ListViewRowItem> list = new ArrayList<ListViewRowItem>();
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://128.199.145.53/tot/getRecord.php");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			Log.d("db", String.format("SELECT * FROM broken WHERE province = '%s' ORDER BY id LIMIT 30", province));
			nameValuePairs.add(new BasicNameValuePair("select", String.format("SELECT * FROM broken WHERE province = '%s' ORDER BY id LIMIT 30", province)));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				InputStream is = entity.getContent();
				StringBuffer sb = new StringBuffer();
				String line = null;

				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				if ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
				
				JSONArray js = new JSONArray(sb.toString());
				for (int i = 0; i < js.length(); i++) {
					JSONObject jo = js.getJSONObject(i);
					ListViewRowItem item = new ListViewRowItem(jo.getString("Id"), jo.getString("Province"), jo.getString("Device"), jo.getString("Ip"), jo.getString("Date"));
					list.add(item);
				}
			} else {
				Toast.makeText(MainActivity.this, "NULL", Toast.LENGTH_SHORT).show();
			}
			Log.d("db", list.size() + "");
			if (list.size() == 0)
				list.add(new ListViewRowItem());
			
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			list.add(new ListViewRowItem());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			list.add(new ListViewRowItem());
			return list;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
