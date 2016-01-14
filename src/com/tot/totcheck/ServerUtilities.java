package com.tot.totcheck;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.*;
 
public final class ServerUtilities {
	
    public static void register(final Context context) {
    	
    	new AsyncTask<Void, Void, String>() {
    		
    		private GoogleCloudMessaging gcmObj;
    		private String regId;
    		
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(context);
                    }
                    regId = gcmObj.register(SharedValues.SENDER_ID);
                    msg = "Registration ID :" + regId;
 
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
 
            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    // Store RegId created by GCM Server in SharedPref
                    storeRegIdinSharedPref(context, regId);
                    Toast.makeText(context, "Registered with GCM Server successfully.nn" + msg, Toast.LENGTH_SHORT).show();
                } else { 
                    Toast.makeText(context, "Reg ID Creation Failed.nnEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time." + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }
    
    public static String getRegIdPref(Context context) {
    	SharedPreferences prefs = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
    	return prefs.getString("regId", null);
    }
    
    private static void storeRegIdinSharedPref(Context context, String regId) {
        SharedPreferences prefs = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("regId", regId);
        editor.commit();
        new AsyncTask<String, Void, Void>() {

			@Override
			protected Void doInBackground(String... params) {
		        storeRegIdinServer(params[0]);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}
        	
        }.execute(regId);
 
    }
    
    private static String storeRegIdinServer(String regId) {
    		String str = "INSERT INTO pushdevice (regId, date) VALUES ('" + regId + "', NOW())";
    		str = str.replace("'", "xxaxx").replace("(", "xxbxx").replace(")", "xxcxx").replace(">", "xxdxx");
    		try {
    			HttpParams httpParameters = new BasicHttpParams();
    			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
    			HttpConnectionParams.setSoTimeout(httpParameters, 10000);

    			HttpClient httpClient = new DefaultHttpClient(httpParameters);
    			HttpPost httpPost = new HttpPost("http://192.168.20.123:8888/reg.php");
    	
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
    				Log.d("gcm", sb.toString());
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