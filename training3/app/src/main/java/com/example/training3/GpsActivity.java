package com.example.training3;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class GpsActivity extends AppCompatActivity {
    private static final String TAG = GpsActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS/2;
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-locationg-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LASAT_UPDATED_TIME_STRING = "last-updated-time-string";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mlocationCallback;
    private Location mCurrentLocation;

    private Button start;
    private Button stop;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView lastUpdateTextView;
    private TextView locationInaTextView;

    private String latitudeLabel;
    private String longitudeLabel;
    private String lastUpdateTimeLabel;
    private boolean mRequestingLocationUpdate;
    private String mLastUpdateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_activity);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);

        latitudeTextView = findViewById(R.id.latitude_text);
        longitudeTextView = findViewById(R.id.longitude_text);
        lastUpdateTextView = findViewById(R.id.last_update_time_text);
        
        latitudeLabel = "Latitude";
        longitudeLabel = "Longitude";
        lastUpdateTimeLabel = "Last location update time";
        
        mRequestingLocationUpdate = false;
        mLastUpdateTime = "";
    }

    private void updateValuesFromBundle(Bundle savedInstanceState){
        if(savedInstanceState!=null){
            if(savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)){
                mRequestingLocationUpdate = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES);
            }

            if(savedInstanceState.keySet().contains(KEY_LOCATION)){
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            if(savedInstanceState.keySet().contains(KEY_LASAT_UPDATED_TIME_STRING)){
                mLastUpdateTime = savedInstanceState.getString(KEY_LASAT_UPDATED_TIME_STRING);
            }
            updateUI();
        }
    }

    private void updateUI() {
        setButtonEnabledState();
        updateLocationUI();
    }

    private void setButtonEnabledState() {
        if(mRequestingLocationUpdate){
            start.setEnabled(false);
            stop.setEnabled(true);
        }else{
            start.setEnabled(true);
            stop.setEnabled(false);
        }
    }

    private void updateLocationUI() {
        if(mCurrentLocation!=null){
            latitudeTextView.setText("Latitude : "+mCurrentLocation.getLatitude());
            longitudeTextView.setText("Longitude : "+mCurrentLocation.getLongitude());
            lastUpdateTextView.setText("Update Time : "+ mLastUpdateTime);
        }
    }

    private void stopLocationUpdates(){
        if(!mRequestingLocationUpdate){
            Log.d(TAG, "StopLocationUpdates: updates never requested, no-op.");
            return;
        }

        fusedLocationProviderClient.removeLocationUpdates(mlocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdate=false;
                        setButtonEnabledState();
                    }
                });
    }
}
