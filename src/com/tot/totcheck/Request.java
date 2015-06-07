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
	
	private Request() {
		
	}
	
	public static String request() {
		return request("");
	}
	
	public static String request(String str) {
		try {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(HOST);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("select", str));
		
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity entity = httpResponse.getEntity();

		if (entity != null) {
			InputStream is = entity.getContent();
			StringBuffer sb = new StringBuffer();
			String line = null;

			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
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
