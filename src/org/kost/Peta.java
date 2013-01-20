package org.kost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mixare.R;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import direction.json.AlertDialogManager;
import direction.json.ConnectionDetector;
import direction.json.GPStracker;
import direction.json.GoogleParser;
import direction.json.JSONParser;
import direction.json.MyItemizedOverlay;
import direction.json.Parser;
import direction.json.Route;

public class Peta extends MapActivity {
	MapController mControl;
	ConnectionDetector cd;
	Context context;
	AlertDialogManager alertD = new AlertDialogManager();
	List<GeoPoint> listPO = new ArrayList<GeoPoint>();
	MapView mapView;

	private Projection proj;
	public static ArrayList<HashMap<String, String>> listkost;
	float mRadius;
	private static String URL = "http://api.juragankost.asia/reqList.php";
	private static final String TAG_ID = "id";
	private static final String TAG_NAMA = "nama";
	private static final String TAG_JENIS = "jenis";
	private static final String TAG_ALAMAT = "alamat";
	private static final String TAG_LAT = "lat";
	private static final String TAG_LON = "lon";
	JSONArray kamar = null;
	GPStracker gps;

	class MapOverlay extends com.google.android.maps.Overlay {

		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			for (HashMap<String, String> lokasi : listkost) {
				Point screenPts = new Point();
				double lat = Double.parseDouble(lokasi.get(TAG_LAT));
				double lon = Double.parseDouble(lokasi.get(TAG_LON));

				GeoPoint gp = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
				proj = mapView.getProjection();

				mapView.getProjection().toPixels(gp, screenPts);
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.marker);

				canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 50, null);

			}
			GeoPoint geo = new GeoPoint((int) (gps.getLatitude() * 1E6),
					(int) (gps.getLongitude() * 1E6));
			Point po = new Point();
			proj.toPixels(geo, po);
			float rad = 5000;
			float crRadius = proj.metersToEquatorPixels(rad);
			Paint iCrcl;
			iCrcl = new Paint();
			iCrcl.setColor(Color.BLUE);
			iCrcl.setAlpha(25);
			iCrcl.setAntiAlias(true);
			iCrcl.setStyle(Paint.Style.FILL);
			canvas.drawCircle((float) po.x, (float) po.y, crRadius, iCrcl);

			mapView.getProjection().toPixels(geo, po);
			Bitmap bitm = BitmapFactory.decodeResource(getResources(),
					R.drawable.marker2);
			canvas.drawBitmap(bitm, po.x, po.y - 50, null);			
			return true;

		}

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_peta);
		gps = new GPStracker(Peta.this);
		cd = new ConnectionDetector(getApplicationContext());
		if (!cd.isConnectingToInternet()) {
			alertD.showAlertDialog(Peta.this, "Internet",
					"Pastikan Perangkat Anda terkoneksi internet", false);
		}
		if (!gps.canGetLocation()) {
			gps.showSettingsAlert();
		}
		listkost = new ArrayList<HashMap<String, String>>();

		String latitude = String.valueOf(gps.getLatitude());
		String longitude = String.valueOf(gps.getLongitude());
		GeoPoint gg = new GeoPoint((int) (gps.getLatitude() * 1E6),
				(int) (gps.getLongitude() * 1E6));
		mRadius = 5;
		JSONParser jp = new JSONParser();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("latitude", latitude));
		params.add(new BasicNameValuePair("longitude", longitude));
		params.add(new BasicNameValuePair("radius", String.valueOf(mRadius)));
		String json = jp.makeHttpRequest(URL, "GET", params);
		Toast.makeText(getApplicationContext(), params.toString(),
				Toast.LENGTH_SHORT).show();
		Log.d(json, "errorrnyaaa >>");
		try {
			kamar = new JSONArray(json);
			if (kamar != null) {
				for (int i = 0; i < kamar.length(); i++) {
					JSONObject c = kamar.getJSONObject(i);

					String id = c.getString(TAG_ID);
					String nama = c.getString(TAG_NAMA);
					String jenis = c.getString(TAG_JENIS);
					String jalan = c.getString(TAG_ALAMAT);
					String lat = c.getString(TAG_LAT);
					String lang = c.getString(TAG_LON);

					HashMap<String, String> map = new HashMap<String, String>();

					map.put(TAG_ID, id);
					map.put(TAG_NAMA, nama);
					map.put(TAG_JENIS, jenis);
					map.put(TAG_ALAMAT, jalan);
					map.put(TAG_LAT, lat);
					map.put(TAG_LON, lang);

					listkost.add(map);

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.displayZoomControls(true);
		mapView.setBuiltInZoomControls(true);
		List<Overlay> overlay = mapView.getOverlays();
		if (overlay.size() > 0) {
			for (Iterator<Overlay> iterator = overlay.iterator(); iterator
					.hasNext();) {
				iterator.next();
				iterator.remove();
			}
		}

		// dproj = mapView.getProjection();

		MapOverlay mapOver = new MapOverlay();
	
		overlay.clear();
		mapView.getOverlays().add(mapOver);
		mapView.getController().setZoom(15);
		mapView.invalidate();
		mapView.getController().animateTo(gg);
		mapView.setTraffic(true);
		mapView.setSatellite(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_peta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.satelit:
			mapView.setTraffic(false);
			mapView.setSatellite(true);
			break;
		case R.id.trafik:
			mapView.setTraffic(true);
			mapView.setSatellite(false);
			break;
		default:
			return super.onOptionsItemSelected(item);

		}
		return false;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public Route rute(final GeoPoint start, final GeoPoint finish) {
		Parser parser;
		final StringBuffer sBuf = new StringBuffer(
				"http://maps.googleapis.com/maps/api/directions/json?");
		sBuf.append("origin=");
		sBuf.append(start.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(start.getLongitudeE6() / 1E6);
		sBuf.append("&destination=");
		sBuf.append(finish.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(finish.getLongitudeE6() / 1E6);
		sBuf.append("&sensor=true&mode=driving");
		parser = new GoogleParser(sBuf.toString());
		Route r = parser.parse();
		return r;
	}

}
