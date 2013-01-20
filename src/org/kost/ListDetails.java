package org.kost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.R;

import direction.json.AlertDialogManager;
import direction.json.ConnectionDetector;
import direction.json.GPStracker;
import direction.json.ImageLoader;
import direction.json.JSONParser;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListDetails extends Activity {
	ConnectionDetector cd;

	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// Progress Dialog
	private ProgressDialog pDialog;

	// gps cek

	GPStracker gps;

	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();

	ArrayList<HashMap<String, String>> lskost;

	// tracks JSONArray
	JSONArray kost = null;
	private final String URL = "http://api.juragankost.asia/selectdetail.php";
	private static final String KEY_NAMA = "nama";
	private static final String KEY_ALAMAT = "alamat";
	private static final String KEY_TIPE = "jenis";
	private static final String KEY_TELP = "telpon";
	private static final String KEY_JUM = "jumlah";
	private static final String KEY_KOS = "kosong";
	private final static String KEY_LAT = "lat";
	private final static String KEY_LON = "lon";
	private static final String KEY_IMG = "gambar";
	private static final String KEY_PEM = "pemilik";

	String idkost = null;
	String nm_kost, nm_almt, nm_jn, nm_telp, nm_lat, nm_lon, nm_gb, nm_pem,
			nm_jum, nm_kos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_details);

		cd = new ConnectionDetector(getApplicationContext());
		gps = new GPStracker(ListDetails.this);
		if (!gps.canGetLocation()) {
			gps.showSettingsAlert();
		}
		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(ListDetails.this, "Internet Error",
					"Pastikan Perangkat Anda terkoneksi internet", false);
			// stop executing code by return
			return;
		}

		// Get kost id
		Intent i = getIntent();
		idkost = i.getStringExtra("id_kost");
		new LoadDetails().execute();
	}

	public void iTelp(View view) {
		if (nm_telp.equals("")) {
			Toast.makeText(getApplicationContext(),
					"Tidak bisa melakukan panggilan", Toast.LENGTH_SHORT)
					.show();
		}
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + nm_telp));
			startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			Log.e("helloandroid dialing example", "Call failed", e);
		}
	}

	public void iRoute(View view) {
		Intent i = new Intent(this, Rute.class);
		i.putExtra("latitude", Double.parseDouble(nm_lat));
		i.putExtra("longitude", Double.parseDouble(nm_lon));
		startActivity(i);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_details, menu);
		return true;
	}

	class LoadDetails extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListDetails.this);
			pDialog.setMessage("Loading details...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", idkost));

			String json = jsonParser.makeHttpRequest(URL, "GET", params);
			Log.d("Loading detail:", json);
			try {
				kost = new JSONArray(json);
				if (kost != null) {
					for (int i = 0; i < kost.length(); i++) {
						JSONObject jObj = kost.getJSONObject(i);
						nm_kost = jObj.getString(KEY_NAMA);
						nm_almt = jObj.getString(KEY_ALAMAT);
						nm_jn = jObj.getString(KEY_TIPE);
						nm_telp = jObj.getString(KEY_TELP);
						nm_jum = jObj.getString(KEY_JUM);
						nm_lat = jObj.getString(KEY_LAT);
						nm_lon = jObj.getString(KEY_LON);
						String stat = jObj.getString(KEY_KOS);
						if (stat.equals("1")) {
							nm_kos = "Iya";
						} else {
							nm_kos = "Tidak";
						}

						nm_pem = jObj.getString(KEY_PEM);
						nm_gb = jObj.getString(KEY_IMG);

					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			// dismiss the dialog after getting song information
			pDialog.dismiss();

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					TextView judul = (TextView) findViewById(R.id.judul);
					TextView nama = (TextView) findViewById(R.id.lNama);
					TextView alamat = (TextView) findViewById(R.id.lAlamat);
					TextView jumlah = (TextView) findViewById(R.id.lJum);
					TextView pemilik = (TextView) findViewById(R.id.lPem);
					ImageView gambar = (ImageView) findViewById(R.id.gambar);
					TextView kosong = (TextView) findViewById(R.id.lKosong);
					ImageLoader il = new ImageLoader(getApplicationContext());
					TextView tLat = (TextView) findViewById(R.id.mLat);
					TextView tLon = (TextView) findViewById(R.id.mLon);

					tLat.setText(nm_lat);
					tLon.setText(nm_lon);
					il.DisplayImage(nm_gb, gambar);
					judul.setText("Detail Untuk ID " + idkost);
					nama.setText("Nama Kost : " + nm_kost);
					alamat.setText("Alamat  : " + nm_almt);
					pemilik.setText("Nama Pemilik: " + nm_pem);
					jumlah.setText("Jumlah Kamar : " + nm_jum);
					kosong.setText("Status Kosong : " + nm_kos);
					setTitle(nm_kost);
				}
			});
		}
	}

}
