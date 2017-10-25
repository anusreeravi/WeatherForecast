package com.android.weatherforecast.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by Anu on 25/10/2017.
 */

public class LocationUpdateService extends Service implements LocationListener {

    public interface LocationStatusListener {
        void onLocationRequested();
        void onLocationUpdated(double latitude, double longitude, float accuracy);
    }

    private LocationStatusListener mStatusListener;

    private final Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGPSTrackingEnabled = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE = 5; // 5 meters

    private static final long MIN_TIME = 1000 * 2 ; // 2 seconds

    protected LocationManager locationManager;

    private String provider_info;

    public LocationUpdateService(Context context) {
        this.mContext = context;
    }

    /**
     * Try to get my current location by GPS or Network Provider
     */
    public void getLocation(LocationStatusListener listener) {
        mStatusListener = listener;
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                this.isGPSTrackingEnabled = true;

                provider_info = LocationManager.GPS_PROVIDER;

            } else if (isNetworkEnabled) {

                this.isNetworkEnabled = true;
                provider_info = LocationManager.NETWORK_PROVIDER;
            }

            if (!provider_info.isEmpty()) {
                locationManager.requestLocationUpdates(
                        provider_info,
                        MIN_TIME,
                        MIN_DISTANCE,
                        this
                );
                if (mStatusListener != null)
                    mStatusListener.onLocationRequested();
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider_info);
                    updateGPSCoordinates();
                }
            }
        }
        catch (SecurityException ex)
        {
            //e.printStackTrace();
        }
        catch (Exception e)
        {
            //e.printStackTrace();
        }
    }

    /**
     * Update  latitude and longitude
     */
    public void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }


    /**
     * Stop using GPS listener
     * Calling this method will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationUpdateService.this);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (mStatusListener != null) {
                mStatusListener.onLocationUpdated(latitude, longitude, location.getAccuracy());
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}