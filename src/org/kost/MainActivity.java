package org.kost;

import org.mixare.R;

import direction.json.GPStracker;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity {
	GPStracker gps;
	LocationManager lm;
	LocationListener ls;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gps = new GPStracker(MainActivity.this);
		
		if(!gps.canGetLocation()){
			gps.showSettingsAlert();
		}
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ls = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
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
				
			}
		};
		String latitude = String.valueOf(gps.getLatitude());
		String longitude=String.valueOf(gps.getLongitude());
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000,ls);
		// Bikin Tab Bro
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, Status.class);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("Status");
		spec.setContent(intent);
		spec.setIndicator("Status",getResources().getDrawable(R.drawable.tab_status));
		
		tabHost.addTab(spec);
		

		

		intent = new Intent().setClass(this, ListKost.class);
		spec = tabHost.newTabSpec("List");
		spec.setContent(intent);
		spec.setIndicator("List",getResources().getDrawable(R.drawable.tab_list));
		tabHost.addTab(spec);

		
		intent = new Intent().setClass(this, Peta.class);
		spec = tabHost.newTabSpec("Peta");
		spec.setContent(intent);
		spec.setIndicator("Peta",getResources().getDrawable(R.drawable.tab_maps));
		tabHost.addTab(spec);
		
		String uurl = "http://api.juragankost.asia/jsonMixare.php?latitude="+latitude+"&longitude="+longitude+"&radius=10";
		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse(uurl),"application/mixare-json");		
		System.out.print(i);
		spec = tabHost.newTabSpec("AR");
		spec.setContent(i);
		spec.setIndicator("AR",getResources().getDrawable(R.drawable.tab_ar));
		tabHost.addTab(spec);

	}
}