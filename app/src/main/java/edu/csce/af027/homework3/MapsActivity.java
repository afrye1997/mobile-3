package edu.csce.af027.homework3;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button takePicture;
    private Button btnClose;
    private Button btnClear;
    private FusedLocationProviderClient mFusedLocationClient;
    ShowPictureFragment showPictureFragment;
    PictureDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showPictureFragment =
                (ShowPictureFragment) getSupportFragmentManager().findFragmentById(R.id.showimagefragment);

        db = Room.databaseBuilder(getApplicationContext(),
                PictureDatabase.class, "picture_database").allowMainThreadQueries().build();

        takePicture = (Button) findViewById(R.id.btnGetPicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePicture();
            }
        });

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllPhotos();
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mapFragment != null) {
            transaction.add(R.id.cfFrameLayout, mapFragment);
            transaction.commit();
        }
        mapFragment.getMapAsync(this);



    }

    public void closePicture(){
       Log.d("hey", "close picture");
       getSupportFragmentManager().beginTransaction().remove(showPictureFragment).commit();

    }

    public void clearAllPhotos(){
        Log.d("hey", "clear all pictures");
        db.pictureDAO().deleteAll();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mapFragment != null) {
            transaction.add(R.id.cfFrameLayout, mapFragment);
            transaction.commit();
        }
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mapFragment != null) {
            transaction.add(R.id.cfFrameLayout, mapFragment);
            transaction.commit();
        }
        mapFragment.getMapAsync(this);

        if(db.pictureDAO().getAllPictures().size()!= 0){
            Log.d("PLZ WORK", db.pictureDAO().getAllPictures().toString());

        }

    }

    private void takePicture() {
        if (mFusedLocationClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("MapsActivity", "No Location Permission");
                return;
            }
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.d("MapsActivity", "" + location.getLatitude() + ":" + location.getLatitude() );

//                        Geocoder geocoder= new Geocoder(getApplicationContext());
//                        List<Address> addresses = null;
//                        String geocoderName= null;
//                        try {
//                            addresses = geocoder.getFromLocation(location.getLatitude() , location.getLatitude() , 1);
//                            Log.d("address", addresses.get(0).getAddressLine(0));
//                            geocoderName= addresses.get(0).getAddressLine(0);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }


                        //CALL CAMERA FRAGMENT HERE
                        CameraFragment cameraFragment =
                                (CameraFragment) getSupportFragmentManager().findFragmentById(R.id.cameraFragment);


                        cameraFragment=cameraFragment.newInstance();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        Bundle bundle = new Bundle();
                        bundle.putDouble("latitude", location.getLatitude());
                        bundle.putDouble("longitude", location.getLongitude());


                        cameraFragment.setArguments(bundle);

                        transaction.add(R.id.cfFrameLayout, cameraFragment);
                        transaction.commit();



                    }
                }
            });
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        // Add a marker in Sydney and move the camera
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick( Marker marker){
                String markerLocation= (String) marker.getTag();
                if(markerLocation != null ){

                    Log.d("MARKER", markerLocation);
                    //  CALL SHOW IMAGE FRAGMENT HERE

                    Picture picClicked= db.pictureDAO().getPicture(markerLocation);
                    Log.d("MARKER", picClicked.toString());


                    Log.d("in main","gonna call pic frag");
                    // pass path of photo here
//                    ShowPictureFragment showPictureFragment =
//                            (ShowPictureFragment) getSupportFragmentManager().findFragmentById(R.id.showimagefragment);

                    showPictureFragment= ShowPictureFragment.newInstance();
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("pic", picClicked);
                    showPictureFragment.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.cfFrameLayout, showPictureFragment);
                    transaction.commit();
                    Log.d("FRAGMENT","show image");





                    return false;
                }
                return true;
            }
        });
        if(db.pictureDAO().getAllPictures().size()!= 0){
            Log.d("PLZ WORK", db.pictureDAO().getAllPictures().toString());
            List<Picture> locationstoShow=db.pictureDAO().getAllPictures();
            if(locationstoShow.size()!=0){
                for(Picture location: locationstoShow){
                    LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(coordinates).title(location.getTitle().substring(0, location.getTitle().indexOf("*")))).setTag(location.getTitle());;
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));

                }
            }

        }




    }
}