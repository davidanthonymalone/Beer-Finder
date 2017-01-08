package dm.pivofinder.adapters;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import dm.pivofinder.R;
import dm.pivofinder.models.Beer;


public class BeerItem  {
    View view;

    public BeerItem(Context context, ViewGroup parent,
                      OnClickListener deleteListener, Beer Beer)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.beer_row, parent, false);
        view.setId(Beer.beerId);

        updateControls(Beer);

        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        imgDelete.setTag(Beer);
        imgDelete.setOnClickListener(deleteListener);
    }

    private void updateControls(Beer Beer) {
        String slat;
        String slng;
        slat = String.valueOf(Beer.lat);
        slng = String.valueOf(Beer.lng);




        //References the code in the xml
        ((TextView) view.findViewById(R.id.rowBeerName)).setText(Beer.name);
        ((TextView) view.findViewById(R.id.rowBar)).setText(Beer.bar);
        ((TextView) view.findViewById(R.id.lngText)).setText(Beer.address);
        ((TextView) view.findViewById(R.id.rowPrice)).setText("€" +
                new DecimalFormat("0.00").format(Beer.price));


}


}
