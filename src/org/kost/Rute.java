package org.kost;

import java.util.Iterator;
import java.util.List;

import org.mixare.lib.R;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import direction.json.GPStracker;
import direction.json.GoogleParser;
import direction.json.MyItemizedOverlay;
import direction.json.Parser;
import direction.json.Route;
import direction.json.RouteOverlay;

public class Rute extends MapActivity {
	Uri uri;
	GPStracker gps;
	MapView mapRoute;
	String lat, lon;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gps = new GPStracker(Rute.this);
		mapRoute =(MapView)findViewById(R.layout.activity_peta);
		Intent i = getIntent();
		double lat = Double.parseDouble(i.getStringExtra("latitude"));
		double lon = Double.parseDouble(i.getStringExtra("longitude"));
		double mLat = gps.getLatitude();
		double mLon = gps.getLongitude();
		List<Overlay> overlays = mapRoute.getOverlays();
		if(overlays.size()>0){
			for(Iterator<Overlay> iterator=overlays.iterator();iterator.hasNext();){
				iterator.next();
				iterator.remove();
			}
		}
		GeoPoint gStart = new GeoPoint((int) (mLat * 1E6), (int) (mLon * 1E6));
		Drawable marker = getResources().getDrawable(R.drawable.marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),marker.getIntrinsicHeight());
		MyItemizedOverlay overlay = new MyItemizedOverlay(marker,this);
		mapRoute.getOverlays().add(overlay);
		GeoPoint gDest = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
		marker = getResources().getDrawable(R.drawable.marker2);
		overlay= new MyItemizedOverlay(marker,this);
		mapRoute.getOverlays().add(overlay);
		Route ruote = directions(gStart, gDest);
		RouteOverlay rOverlay = new RouteOverlay(ruote, Color.BLUE);
		mapRoute.getOverlays().add(rOverlay);
		mapRoute.getController().animateTo(gStart);
		mapRoute.postInvalidate();
		mapRoute.displayZoomControls(true);
		mapRoute.setBuiltInZoomControls(true);
		mapRoute.getController().setZoom(15);

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
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