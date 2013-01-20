package org.kost;

import org.mixare.R;

import android.location.Location;
import android.os.Bundle;

import android.view.Menu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import direction.json.AlertDialogManager;
import direction.json.ConnectionDetector;
import direction.json.GPStracker;
import direction.json.JSONParser;
import direction.json.LazyAdapter;


import android.os.AsyncTask;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import android.widget.TextView;


public class ListKost extends ListActivity {
	GPStracker gps = new GPStracker(this);
	
	ConnectionDetector cd;

	AlertDialogManager alert = new AlertDialogManager();

	private ProgressDialog pDialog;

	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> lskost;
	JSONArray aKost = null;
	LazyAdapter adapter;
	public static final String URL = "http://api.juragankost.asia/reqList.php";
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "nama";
	public static final String KEY_ALM = "alamat";
	public static final String KEY_TIPE = "jenis";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LON = "lon";
	public static final String KEY_HARGA = "harga";
	public static final String KEY_IMG = "gambar";
	public static final String KEY_JARAK="";
	// String idkost;
	private Location myloc;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listkost);
		cd = new ConnectionDetector(getApplicationContext());
		gps = new GPStracker(ListKost.this);
		if(!gps.canGetLocation()){
			gps.showSettingsAlert();
		}
		double mylat = gps.getLatitude();
		double mylon = gps.getLongitude();		
		myloc = new Location("Posisi");
		myloc.setLatitude(mylat);
		myloc.setLongitude(mylon);
		
		if (!cd.isConnectingToInternet()) {
			alert.showAlertDialog(ListKost.this, "Internet Bermasalah !",
					"Pastikan Terkoneksi Internet", false);
			return;
		}

		lskost = new ArrayList<HashMap<String, String>>();
		new LoadKost().execute();
		ListView lv = getListView();
		lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {

				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(),
						ListDetails.class);
				String id_kost = ((TextView) view.findViewById(R.id.id_kost))
						.getText().toString();
				i.putExtra("id_kost", id_kost);
				startActivity(i);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.listkost, menu);
		return true;
	}

	class LoadKost extends AsyncTask<String, String, String> {
		// jalankan mode background

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListKost.this);
			pDialog.setMessage("Loading daftar kost ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		// ambil data
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			String latitude = String.valueOf(gps.getLatitude());
			String longitude = String.valueOf(gps.getLongitude());
			String radius = "5";
			String cari ="";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("q",cari));
			params.add(new BasicNameValuePair("latitude",latitude));
			params.add(new BasicNameValuePair("longitude",longitude));
			params.add(new BasicNameValuePair("radius",radius));
			String json = jParser.makeHttpRequest(URL, "GET", params);
			Log.d("Jsonnya bro: ", "> " + json);
			try {
				aKost = new JSONArray(json);
				if (aKost != null) {
					for (int i = 0; i < aKost.length(); i++) {

						JSONObject c = aKost.getJSONObject(i);

						String id = c.getString(KEY_ID);
						String nama = c.getString(KEY_NAME);
						String jenis = c.getString(KEY_TIPE);
						String alamat = c.getString(KEY_ALM);
						String img = c.getString(KEY_IMG);
						String harga = c.getString(KEY_HARGA);
						Location locB = new Location("Lokasi B");
						double bLat =Double.parseDouble(c.getString(KEY_LAT));
						double bLon =Double.parseDouble(c.getString(KEY_LON));
						locB.setLatitude(bLat);
						locB.setLongitude(bLon);
						
						String jarak = Double.toString(myloc.distanceTo(locB)/1000);
						HashMap<String, String> map = new HashMap<String, String>();
						
						map.put(KEY_ID, id);
						map.put(KEY_NAME, nama);
						map.put(KEY_TIPE, jenis);
						map.put(KEY_ALM, alamat);
						map.put(KEY_IMG, img);
						map.put(KEY_HARGA, harga);
						map.put(KEY_JARAK, jarak);
						lskost.add(map);
					}
				} else {
					Log.d("Kost : ", "null");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					/*
					 * ListAdapter adapter = new SimpleAdapter(ListKost.this,
					 * lskost, R.layout.list_data, new String[] { KEY_ID,
					 * KEY_NAME, KEY_TIPE }, new int[] { R.id.lID, R.id.lnm,
					 * R.id.lJenis }); setListAdapter(adapter);
					 */
					adapter = new LazyAdapter(ListKost.this, lskost);
					setListAdapter(adapter);
				}
			});
		}
		
		
	}
	

}
