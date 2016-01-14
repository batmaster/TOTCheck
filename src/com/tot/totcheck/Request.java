package com.tot.totcheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

public class Request {
	
	private Request() {
		
	}
	
	public static String requestProvincesAndAmount() throws IOException {
		return request("SELECT s.province AS province, SUM(CASE WHEN smsdown = 'yes' AND smsup = '' THEN 1 ELSE 0 END) AS amount FROM sector s, nodeumbo n WHERE n.node_sector = s.umbo GROUP BY s.province ORDER BY s.province");
	}
	
	public static String requestDownList(Context context) throws IOException {
		return request(String.format("SELECT n.*, s.province FROM nodeumbo n, sector s WHERE n.node_sector = s.umbo AND s.province IN ((SELECT province WHERE pushdevice_id = (SELECT id FROM pushdevice WHERE regId = %s))) AND smsdown = 'yes' AND smsup = ''", SharedValues.getStringPref(context, SharedValues.KEY_REGID)));
	}
	
	public static String requestDownList(String province) throws IOException {
		return request(String.format("SELECT n.*, s.province FROM nodeumbo n, sector s WHERE n.node_sector = s.umbo AND s.province IN (%s) AND smsdown = 'yes' AND smsup = ''", province));
	}
	
	public static String request(String str) throws IOException {
		str = str.replace("'", "xxaxx").replace("(", "xxbxx").replace(")", "xxcxx").replace(">", "xxdxx");
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			HttpConnectionParams.setSoTimeout(httpParameters, 10000);

			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(SharedValues.HOST_DB);
	
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("sql", str));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
	
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
	
			if (entity != null) {
				InputStream is = entity.getContent();
				StringBuffer sb = new StringBuffer();
				String line = null;
	
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line + "!!!");
				}
				reader.close();
	
				return sb.toString();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getVersion() throws IOException {
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
			HttpConnectionParams.setSoTimeout(httpParameters, 10000);

			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(SharedValues.HOST_VERSION);
	
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("version", ""));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
	
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
	
			if (entity != null) {
				InputStream is = entity.getContent();
				StringBuffer sb = new StringBuffer();
				String line = null;
	
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line + ",");
				}
				reader.close();
	
				return sb.toString();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		}
		
		return "";
	}

	public static void setStateProvince(Context context, String province, boolean state) throws IOException {
		String regId = SharedValues.getStringPref(context, SharedValues.KEY_REGID);
		String sql = "";
		if (state)
			sql = String.format("INSERT INTO province_pref (pushdevice_id, province) VALUES ((SELECT id FROM pushdevice WHERE regId = '%s'), '%s')", regId, province);
		else
			sql = String.format("DELETE FROM province_pref WHERE pushdevice_id = (SELECT id FROM pushdevice WHERE regId = '%s') AND province = '%s'", regId, province);
		
		
		request(sql);
	}
}
