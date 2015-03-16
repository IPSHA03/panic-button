package telefonicabeta.panicbutton;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Google maps panic class
 */
public class PanicMap {
    private LocationManager locationManager;
    private Location location;
    private Activity activity;
    private GoogleMap map;
    private Marker marker;
    final private Integer DEFAULT_ZOOM = 15;

    public PanicMap(Activity activity) {
        this.setActivity(activity);
        this.setLocationManager();

        // Initialize the map
        this.setMap();
        this.setMapLocation();
        this.setMarker();
        this.startLocationRequest();
    }


    /**
     * Set marker
     */
    public void setMarker() {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(this.getLatLng());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

        this.marker = map.addMarker(markerOptions);
    }

    /**
     * Get marker
     * @return marker
     */
    public Marker getMarker() {
        return this.marker;
    }

    /**
     * Set location manager
     */
    private void setLocationManager() {
        Activity activity = getActivity();
        this.locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
    }

    /**
     * Set location listener
     */
    private void setLocationlistener() {
        final Marker marker = this.getMarker();

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                marker.setPosition(newLocation);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, DEFAULT_ZOOM));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        this.getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    /**
     * Get location manager
     * @return locationMananger
     */
    private LocationManager getLocationManager() {
        return this.locationManager;
    }

    /**
     * Set location
     */
    private void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Get location
     * @return location
     */
    public Location getLocation() {
        if (getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            this.location = getLocationManager().getLastKnownLocation(LocationManager.GPS_PROVIDER);

        } else {
            this.location = getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        return this.location;
    }

    /**
     * Set Activity
     */
    private void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Get activity
     * @return activity
     */
    private Activity getActivity() {
        return this.activity;
    }

    /**
     * Set google maps
     */
    private void setMap() {
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        this.map = mapFragment.getMap();
    }

    /**
     * Get google maps
     * @return map
     */
    public GoogleMap getMap() {
        return map;
    }

    /**
     * Set MAP location
     */
    public void setMapLocation() {
        this.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(this.getLatLng(), DEFAULT_ZOOM));
    }

    /**
     * Get the lat long based on location
     * @return latlng
     */
    public LatLng getLatLng() {
        Location location = this.getLocation();

        if (location != null) {
            return new LatLng(location.getLatitude(), location.getLongitude());

        } else {
            return new LatLng(0, 0);
        }
    }

    /**
     * Set marker icon
     */
    public void setMarkerIcon(String icon) {
        Marker marker = this.getMarker();

        if (icon == "gray") {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_gray));

        } else {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        }
    }

    /**
     * start location request interval
     */
    protected void startLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
}
