package com.tot.totcheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Request {

//	private static final String HOST = "http://128.199.145.53/tot/getRecord.php";
	private static final String HOST = "http://203.114.104.242/umbo/getRecord.php";
	
	public static final String REQ_GET_PROVINCES = "SELECT s.province AS province, SUM(CASE WHEN smsdown = 'yes' AND smsup = '' THEN 1 ELSE 0 END) AS amount FROM sector s, nodeumbo n WHERE n.node_sector = s.umbo GROUP BY s.province ORDER BY s.province".replace("'", "xxaxx");
	public static final String REQ_DEFAULT = "";
	private static final String REQ_GET_LIST_PROVINCE = "SELECT n.*, s.province FROM nodeumbo n, sector s WHERE n.node_sector = s.umbo AND s.province IN (%s) AND smsdown = 'yes' AND smsup = '' AND n.id_nu > %d ORDER BY n.id_nu DESC".replace("'", "xxaxx").replace("(", "xxbxx").replace(")", "xxcxx");
	
	private Request() {
		
	}
	
	public static String requestList(String[] provinces) {
		return requestList(provinces, 0);
	}
	
	public static String requestList(String[] provinces, int startId) {
		String arg = "";
		for (int i = 0; i < provinces.length; i++) {
			arg += "'" + provinces[i] + "'";
			if (i != provinces.length - 1)
				arg += ",";
		}
		return request(String.format(REQ_GET_LIST_PROVINCE, arg, startId));
	}
	
	public static String request() {
		return request(REQ_DEFAULT);
	}
	
	public static String request(String str) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(HOST);
	
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
