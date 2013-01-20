package org.kost;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.mixare.R;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Status extends Activity {
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // dalam
																		// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // dalam
																	// Milliseconds

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);

		setStatus();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_status, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.updat:
			setStatus();
			return true;
		case R.id.atur:
			Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			this.startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private final LocationListener lListener = new LocationListener() {
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
			cekGPS(null);
		}

		@Override
		public void onLocationChanged(Location loc) {
			// TODO Auto-generated method stub
			cekGPS(loc);
		}
	};
	private void setStatus(){
		LocationManager lm;
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location lokasi = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, lListener);
		cekGPS(lokasi);
	}
	private void cekGPS(Location loc) {
		TextView tgps = (TextView) findViewById(R.id.lGps);
		TextView tlat = (TextView) findViewById(R.id.lLat);
		TextView tlon = (TextView) findViewById(R.id.lLon);
		TextView ttip = (TextView) findViewById(R.id.lTips);
		TextView tjal = (TextView) findViewById(R.id.lLoc);
		String alamat = "Alamat tidak diketahui";
		// cek posisi
		if (loc != null) {
			double lat = loc.getLatitude();
			double lon = loc.getLongitude();

			tgps.setText("GPS = Aktif \n");
			tlat.setText("Latitude = " + lat + "\n");
			tlon.setText("Longitude = " + lon + "\n");
			Geocoder gc = new Geocoder(Status.this, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(lat, lon, 10);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = addresses.get(0);

					for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
						sb.append(address.getAddressLine(i)).append(", ");
					sb.append(address.getLocality()).append("\n");
				}else{
					sb.append("Unknown");
				}
				alamat = sb.toString();
			} catch (IOException e) {
			}
			tjal.setText("Lokasi = " + alamat);
			ttip.setText("Tips : Tekan Menu - Refresh untuk meng-update status Anda");
		} else {
			tgps.setText("GPS = Non Aktif");
			tlat.setText("Latitude = Tidak diketahui");
			tlon.setText("Longitude = Tidak diketahui");
			tjal.setText("Lokasi = Tidak diketahui");
			ttip.setText("Tips : Nyalakan GPS. Tekan Menu - setting");
		}
		// end posisi

	}

}
