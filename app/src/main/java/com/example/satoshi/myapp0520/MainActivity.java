package com.example.satoshi.myapp0520;


import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;


import java.net.URLEncoder;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText et;
    Button bt1;
    String name, Slat, Slng;
    Integer lat = 0, lng = 0;
    GoogleMap map;
    Activity a;
    CameraUpdate cu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IdSearch();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();


        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
        }

        cu = CameraUpdateFactory.newLatLngZoom(new LatLng(43.0675, 141.350784), 15);
        map.animateCamera(cu);
    }

    public void IdSearch() {
        et = (EditText) findViewById(R.id.et);
        bt1 = (Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et.getText().toString();
                try {
                    String Ename = URLEncoder.encode(name, "UTF-8");
                    getLL(Ename);
                } catch (Exception e) {
                }


            }
        });

    }

    public void getLL(final String name) {

        String url = "http://www.geocoding.jp/api/?v=1.1&q=" + name;
        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... url) {
                try {

                    Builder builder = new Builder();
                    Document doc = builder.build(url[0]);
                    Element root = doc.getRootElement();
                    Element coordinate = root.getFirstChildElement("coordinate");
                    Slat = coordinate.getFirstChildElement("lat").getValue();
                    Slng = coordinate.getFirstChildElement("lng").getValue();

                } catch (Exception e) {
                    return "失敗";
                }
                return "成功";
            }
            @Override
            protected void onPostExecute(String result) {
                moveTo(Double.parseDouble(Slat), Double.parseDouble(Slng));
            }
        }.execute(url);

    }


    public void onMapReady(GoogleMap m) {
        // Add a marker in Sydney, Australia, and move the camera.
        m.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng sydney = new LatLng(-34, 151);
        m.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        m.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    protected void moveTo(Double lat, Double lng) {
        cu = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15);
        map.animateCamera(cu);

    }

}





