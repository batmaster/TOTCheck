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

public class Request {
	
	public static final String REQ_GET_PROVINCES = "SELECT s.province AS province, SUM(CASE WHEN smsdown = 'yes' AND smsup = '' THEN 1 ELSE 0 END) AS amount FROM sector s, nodeumbo n WHERE n.node_sector = s.umbo GROUP BY s.province ORDER BY s.province";
	public static final String REQ_DEFAULT = "";
	private static final String REQ_GET_DOWNLIST = "SELECT n.*, s.province FROM nodeumbo n, sector s WHERE n.node_sector = s.umbo AND s.province IN (%s) AND smsdown = 'yes' AND smsup = '' AND n.id_nu > %d ORDER BY n.id_nu DESC";
	private static final String REQ_GET_UPLIST = "SELECT n.*, s.province FROM nodeumbo n, sector s WHERE n.node_sector = s.umbo AND n.id_nu IN (%s) AND smsdown = 'yes' AND smsup = 'yes' ORDER BY n.id_nu DESC";
	
	private Request() {
		
	}
	
	public static String requestUpList(ArrayList<Integer> upList) throws ConnectTimeoutException, SocketTimeoutException, HttpHostConnectException {
		String arg = "";
		for (int i = 0; i < upList.size(); i++) {
			arg += upList.get(i);
			if (i != upList.size() - 1)
				arg += ",";
		}
		if (upList.size() == 0)
			arg += "0";
		
		return request(String.format(REQ_GET_UPLIST, arg));
	}
	
	public static String requestDownList(String[] provinces) throws ConnectTimeoutException, SocketTimeoutException, HttpHostConnectException {
		return requestDownList(provinces, 0);
	}
	
	public static String requestDownList(String[] provinces, int startId) throws ConnectTimeoutException, SocketTimeoutException, HttpHostConnectException {
		String arg = "";
		for (int i = 0; i < provinces.length; i++) {
			arg += "'" + provinces[i] + "'";
			if (i != provinces.length - 1)
				arg += ",";
		}
		if (provinces.length == 0)
			arg += "'z'";
		
		return request(String.format(REQ_GET_DOWNLIST, arg, startId));
	}
	
	public static String request() throws ConnectTimeoutException, SocketTimeoutException, HttpHostConnectException {
		return request(REQ_DEFAULT);
	}
	
	public static String request(String str) throws org.apache.http.conn.ConnectTimeoutException, java.net.SocketTimeoutException, org.apache.http.conn.HttpHostConnectException {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getVersion() throws ConnectTimeoutException, SocketTimeoutException, HttpHostConnectException {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
