package direction.json;

import java.util.ArrayList;

import org.mixare.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

@SuppressWarnings("rawtypes")
public class MyItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
	private Context mContext;
	double mLat;
	double mLon;
	float mRadius;
	public MyItemizedOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}

	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int index) {
		return items.get(index);
	}

	@Override
	public int size() {
		return items.size();

	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);

	}

	public void addItem(OverlayItem item) {
		items.add(item);
		populate();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = items.get(0);

		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.setPositiveButton("Close", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		dialog.show();		
		return true;
	}

}
