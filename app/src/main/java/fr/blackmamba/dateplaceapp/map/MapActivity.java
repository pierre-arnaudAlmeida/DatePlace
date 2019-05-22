package fr.blackmamba.dateplaceapp.map;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import java.util.List;

import fr.blackmamba.dateplaceapp.place.PlaceActivity;
import fr.blackmamba.dateplaceapp.R;
import fr.blackmamba.dateplaceapp.profile.UserProfilActivity;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener {
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionManager;
    private Button button;
    private ImageView button_profil;
    private ImageView button_research;
    private ImageButton localisation_button;

    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Mapbox.getInstance(getApplicationContext(), getString(R.string.access_token));
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap ->
                map = mapboxMap);

        mapView.getMapAsync(this);
        /**this.button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            /**
             * Si l'utilisateur clique sur le bouton retour, il sera redirigé vers l'activité
             * SearchActivity
             * @param v

            @Override
            public void onClick(View v) {
                Intent button = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(button);
                finish();
            }
        });*/

        // TODO faire la localisation quand on clique sur ce bouton
        //la ca renvoie vers la page d'un lieux
        this.localisation_button = findViewById(R.id.image_button);
        localisation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_place = new Intent(getApplicationContext(), PlaceActivity.class);
                startActivity(go_place);
                finish();
            }
        });

        this.button_profil = findViewById(R.id.imageView);
        button_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_profil = new Intent(getApplicationContext(), UserProfilActivity.class);
                startActivity(go_profil);
                finish();
            }
        });

        this.button_research = findViewById(R.id.button_research);
        button_research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent research = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(research);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        enableLocation();
    }

    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();
            initializeLocationLayer();
        }else {
            permissionManager = new PermissionsManager(this);
            permissionManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")

    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location LastLocation = locationEngine.getLastLocation();
        if (LastLocation !=null) {
            originLocation = LastLocation;
            setCameraPosition(LastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer() {
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }


    private  void setCameraPosition(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 13.0));
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location !=null) {
            originLocation = location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        // Present Toast or dialog
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted) {
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine !=null) {
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin != null) {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
