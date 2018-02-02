package com.example.acn0036.schedulertest.AddSchedule;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.acn0036.schedulertest.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GetLocation mGetLocation;
    private Button mCancel, mSearch;
    private EditText mEditAddress;
    private List<Address> address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "onCreate");

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mEditAddress = (EditText) findViewById(R.id.edit_search_map);
        mSearch = (Button) findViewById(R.id.btn_find);
        mCancel = (Button) findViewById(R.id.btn_mapcancel);

        final Geocoder mGeoCoder;

        mGeoCoder = new Geocoder(MapsActivity.this);

        //주소로 위도, 경도 검색
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = mEditAddress.getText().toString();
                Log.e("str", str);
                try {
                    address = mGeoCoder.getFromLocationName(str, 1);   //str->위도, 경도
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("fail", "주소 변환 실패");
                }

                if (address != null && address.size() > 0) {
                    Address addr = address.get(0);
                    double lat = addr.getLatitude(); //위도
                    double lon = addr.getLongitude(); //경도

                    LatLng position = new LatLng(lat, lon);

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(position).title("here").draggable(true));
                } else {
                    Log.e("err", "주소 없음");
                }
            }
        });

        //액티비티 종료
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        Log.e("onMapReady", "onMapReady");
        mMap = googleMap;
        mGetLocation = new GetLocation();

        GetLocation.LocationResult locationResult = new GetLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                mMap.clear();

                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude()); //현재 위치

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

                //현재 위치에 마커 추가
                mMap.addMarker(new MarkerOptions().position(currentPosition).title("here").draggable(true));
            }
        };

        //marker click listener : 위치 정보 전송
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng location = marker.getPosition();
                double lat, lng;
                List<Address> getLocation = null;
                String locationName;

                final Geocoder mGeoCoder = new Geocoder(MapsActivity.this);

                lat = location.latitude;
                lng = location.longitude;
                Log.e("location", "lat: " + lat + " lng: " + lng);

                Log.e("geocoder is present", mGeoCoder.isPresent()+"");

                try {
                    getLocation = mGeoCoder.getFromLocation(lat, lng, 1); //위도, 경도->주소
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("fail", "주소 변환 실패");
                }

                if (getLocation != null && getLocation.size() > 0) {
                    locationName = getLocation.get(0).getAddressLine(0);
                    Log.e("locationName", locationName);

                    Intent intent = new Intent();
                    intent.putExtra("location", locationName);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Log.e("err", "주소 없음");
                }

                return false;
            }
        });

        mGetLocation.getLocation(getApplicationContext(), locationResult);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

    }
}