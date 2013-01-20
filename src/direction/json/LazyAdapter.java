package direction.json;

import java.util.ArrayList;
import java.util.HashMap;

import org.kost.ListKost;
import org.mixare.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_data, null);
		TextView id_kost = (TextView) vi.findViewById(R.id.id_kost); // idkost
		TextView title = (TextView) vi.findViewById(R.id.title); // titel
		TextView jenis = (TextView) vi.findViewById(R.id.jenis); // jenis
		TextView detail = (TextView) vi.findViewById(R.id.detail); // detail
																	// alamat
		TextView harga = (TextView) vi.findViewById(R.id.harga);
		
		TextView jarak = (TextView)vi.findViewById(R.id.jarak); // jarak
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb
																				// gambar	
		HashMap<String, String> kost = new HashMap<String, String>();
		kost = data.get(position);

		// Setting all values in listview
		title.setText(kost.get(ListKost.KEY_NAME));
		jenis.setText(kost.get(ListKost.KEY_TIPE));
		detail.setText(kost.get(ListKost.KEY_ALM));
		id_kost.setText(kost.get(ListKost.KEY_ID));
		harga.setText(kost.get(ListKost.KEY_HARGA));
		jarak.setText(kost.get(ListKost.KEY_JARAK));
		imageLoader.DisplayImage(kost.get(ListKost.KEY_IMG), thumb_image);		
		return vi;
	}
}