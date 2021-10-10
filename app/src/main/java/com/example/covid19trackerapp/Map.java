package com.example.covid19trackerapp;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.widget.SearchView;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Map extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
  // String url="https://corona.lmao.ninja/v2/countries";
    Context context;
    List<WeightedLatLng> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout file as the content view.
        context=this;
        setContentView(R.layout.activity_map);
        searchView=findViewById(R.id.sv_location);
        // Get a handle to the fragment and register the callback.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location=searchView.getQuery().toString();
                List<Address> addressList=null;

                if(location!=null || !location.equals("")) {
                    Geocoder geocoder=new Geocoder(Map.this);
                    try{
                        addressList=geocoder.getFromLocationName(location,1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Address address=addressList.get(0);
                    LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);

    }




    private List<WeightedLatLng> addHeatMap() {
        List<WeightedLatLng> latLngs = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            latLngs = readItems(R.raw.check);
        } catch (JSONException e) {
            Toast.makeText(context, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }
        return latLngs;
    }

    private List<WeightedLatLng> readItems(@RawRes int resource) throws JSONException{
        result = new ArrayList<>();
        InputStream inputStream = context.getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("long");
            double density=object.getDouble("cases");
            result.add(new WeightedLatLng(new LatLng(lat, lng),density));
        }
        return result;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        result = addHeatMap();
        map= googleMap;
        HeatmapTileProvider heatmapprovider = new HeatmapTileProvider.Builder()
                .weightedData(result) // load our weighted data
                .radius(22) // optional, in pixels, can be anything between 20 and 50
                .maxIntensity(1000.0) // set the maximum intensity
                .build();

        TileOverlay tileOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatmapprovider));
        LatLng indiaLatLng = new LatLng(28.7041, 77.1025);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLatLng, 10f));
    }
}
/* private List<WeightedLatLng> addHeatMap(GoogleMap googleMap) {
        List<WeightedLatLng> latLngs = null;

        readItems(googleMap);

        return latLngs;
    }

    private void readItems(GoogleMap googleMap){
        result = new ArrayList<>();

        RequestQueue Queue = Volley.newRequestQueue(Map.this);
        Request request = new JsonObjectRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    for (int i = 0; i < 223; i++) {
                        JSONArray array = response.getJSONArray("countryInfo");
                        JSONObject object = array.getJSONObject(i);
                        double lat = object.getDouble("lat");
                        double lng = object.getDouble("long");
                        result.add(new WeightedLatLng(new LatLng(lat, lng)));
                    }
                    Toast.makeText(Map.this, result+"", Toast.LENGTH_SHORT).show();
                    HeatmapTileProvider heatmapprovider = new HeatmapTileProvider.Builder()
                            .weightedData(result) // load our weighted data
                            .radius(50) // optional, in pixels, can be anything between 20 and 50
                            .maxIntensity(1000.0) // set the maximum intensity
                            .build();

                    googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatmapprovider));
                    LatLng indiaLatLng = new LatLng(29.218264, 79.512978);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLatLng, 14f));

                } catch (JSONException e) {
                    Toast.makeText(context, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Queue.add(request);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        addHeatMap(googleMap);
    }*/
