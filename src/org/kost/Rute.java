package org.kost;

import java.util.Iterator;
import java.util.List;

import org.mixare.lib.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import direction.json.GPStracker;
import direction.json.GoogleParser;
import direction.json.MapDirection;
import direction.json.MyItemizedOverlay;
import direction.json.Parser;
import direction.json.Route;
import direction.json.RouteOverlay;

public class Rute extends MapActivity {
	private MapView mapView;
	private LocationManager lm;
	private LocationListener ls;
	MapController myMC = null;
	GeoPoint g = null;
	Uri uri;
	GPStracker gps;
	MapView mapRoute;
	String lat, lon;
	double mLat, mLon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.rute);
		mapView = (MapView) findViewById(R.id.mapRute);
		gps = new GPStracker(this);
		Intent i = getIntent();
		lat = i.getStringExtra("latitude");
		lon = i.getStringExtra("longitude");
		//Toast.makeText(getApplicationContext(), lat+"<>"+lon, Toast.LENGTH_SHORT).show();
		mLat = Double.parseDouble(lat);
		mLon = Double.parseDouble(lon);
		//tampilRute();
		//getGPS();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void getGPS() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ls = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				tampilPeta(location);
			}
		};
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, ls);
	}

	protected void tampilPeta(Location myloc) {
		List<Overlay> overlays = mapView.getOverlays();
		if (overlays.size() > 0) {
			for (Iterator<Overlay> iterator = overlays.iterator(); iterator
					.hasNext();) {
				iterator.next();
				iterator.remove();
			}
		}

		GeoPoint gp = new GeoPoint((int) (myloc.getLatitude() * 1E6),
				(int) (myloc.getLongitude() * 1E6));
		Location locA = new Location("Posisi Awal");
		locA.setLatitude(gp.getLatitudeE6() / 1E6);
		locA.setLongitude(gp.getLongitudeE6() / 1E6);
		Drawable icon = getResources().getDrawable(R.drawable.marker);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(),
				icon.getIntrinsicHeight());
		MyItemizedOverlay overlay = new MyItemizedOverlay(icon, this);
		OverlayItem item = new OverlayItem(gp, "Posisi Awal", "posisi start");
		overlay.addItem(item);
		mapView.getOverlays().add(overlay);

		GeoPoint dest = new GeoPoint((int) (mLat * 1E6), (int) (mLon * 1E6));
		Location locB = new Location("Lokasi Tujuan");
		locB.setLatitude(dest.getLatitudeE6() / 1E6);
		locB.setLongitude(dest.getLongitudeE6() / 1E6);
		icon = getResources().getDrawable(R.drawable.marker2);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(),
				icon.getIntrinsicHeight());
		overlay = new MyItemizedOverlay(icon, this);
		item = new OverlayItem(dest, "Tujuan", "GP tujuan");
		overlay.addItem(item);
		mapView.getOverlays().add(overlay);
		Route rute = directions(gp, dest);
		RouteOverlay rOverlay = new RouteOverlay(rute, Color.BLUE);
		mapView.getOverlays().add(rOverlay);
		mapView.getController().animateTo(gp);
		mapView.postInvalidate();
		mapView.displayZoomControls(true);
		mapView.setBuiltInZoomControls(true);
		mapView.getController().setZoom(15);
	}
	private void tampilRute(){
		GeoPoint gp = new GeoPoint((int) (gps.getLatitude() * 1E6),
				(int) (gps.getLongitude() * 1E6));
		
		//mapView.getOverlays().add(new MapDirection(gp, gp));

		GeoPoint dest = new GeoPoint((int) (mLat * 1E6), (int) (mLon * 1E6));
		Location locB = new Location("Lokasi Tujuan");
		locB.setLatitude(dest.getLatitudeE6() / 1E6);
		locB.setLongitude(dest.getLongitudeE6() / 1E6);
		
		mapView.getOverlays().add(new MapDirection(gp, dest));
		
		
		mapView.getController().animateTo(gp);
		mapView.postInvalidate();
		mapView.displayZoomControls(true);
		mapView.setBuiltInZoomControls(true);
		mapView.getController().setZoom(15);
	}
	private Route directions(final GeoPoint start, final GeoPoint dest) {
		Parser parser;
		final StringBuffer sBuf = new StringBuffer(
				"http://maps.googleapis.com/maps/api/directions/json?");
		sBuf.append("origin=");
		sBuf.append(start.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(start.getLongitudeE6() / 1E6);
		sBuf.append("&destination=");
		sBuf.append(dest.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(dest.getLongitudeE6() / 1E6);
		sBuf.append("&sensor=true&mode=driving");
		parser = new GoogleParser(sBuf.toString());
		Route r = parser.parse();
		return r;
	}
}