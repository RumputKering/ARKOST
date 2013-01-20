package direction.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class jsParser {
	
	InputStream is = null;
	JSONObject jobj = null;
	String json = "";
	
	public JSONObject AmbilJson(String url) {
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			
		} catch (UnsupportedEncodingException e){
			Log.e("UNSUPPORTED", e.getMessage());
		}catch (ClientProtocolException e){
			Log.e("CLIENT", e.getMessage());
		}catch (IOException e){
			Log.e("IO", e.getMessage());
		}
		
		try {
			Log.d("TEST", "BEFORE START");
			BufferedReader reader = new BufferedReader (new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb= new StringBuilder();
			String line = null;
			Log.d("TEST", "START");
			while ((line = reader.readLine()) !=null){
				sb.append(line);
				Log.d("TEST", line);
			}
			Log.d("TEST", "END");
			is.close();
			json = sb.toString();
			
		} catch (Exception e){
			Log.e("JSON Parser", "Error pasing data " + e.toString() + e.getMessage());
		}
		try {
			jobj = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jobj;
			}
			
		
	}
	

