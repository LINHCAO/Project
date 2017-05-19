package com.example.nhat.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    Spinner spCity, spDistrict;
    ArrayAdapter adapterCity, adapterDistrict;
    ImageView imgPhoto;
    EditText edtWeight, edtPhone, edtDeparture, edtDestination;
    FloatingActionButton btnAccept;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        addControls();
        addEvents();
    }

    private void addControls() {
        spCity = (Spinner) findViewById(R.id.spCity);
        spDistrict = (Spinner) findViewById(R.id.spDistrict);

        adapterCity = ArrayAdapter.createFromResource
                (this, R.array.city_arr, android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapterCity);

        adapterDistrict = ArrayAdapter.createFromResource
                (this, R.array.district_arr, android.R.layout.simple_spinner_item);
        adapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistrict.setAdapter(adapterDistrict);

        imgPhoto = (ImageView) findViewById(R.id.imPhoto);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtWeight = (EditText) findViewById(R.id.edtWeight);
        edtDeparture = (EditText) findViewById(R.id.edtDeparture);
        edtDestination = (EditText) findViewById(R.id.edtDestination);
        btnAccept = (FloatingActionButton) findViewById(R.id.btnAccept);
    }

    private void addEvents() {
        btnAccept.setOnClickListener(new View.OnClickListener() {
            Customer customer = new Customer();
            @Override
            public void onClick(View v) {

                if (edtWeight.getText().toString().isEmpty()){
                    Toast.makeText(MapsActivity.this, "Please enter weight", Toast.LENGTH_SHORT).show();
                    edtWeight.requestFocus();
                    return;
                }

                if (edtPhone.getText().toString().isEmpty()){
                    Toast.makeText(MapsActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                    edtPhone.requestFocus();
                    return;
                }

                if (edtDestination.getText().toString().isEmpty()){
                    Toast.makeText(MapsActivity.this, "Please enter destination", Toast.LENGTH_SHORT).show();
                    edtDestination.requestFocus();
                    return;
                }

                if (edtDeparture.getText().toString().isEmpty()){
                    Toast.makeText(MapsActivity.this, "Please enter departure", Toast.LENGTH_SHORT).show();
                    edtDeparture.requestFocus();
                    return;
                }



                customer.city = spCity.getSelectedItem().toString();
                customer.district = spDistrict.getSelectedItem().toString();

                customer.weight = Float.valueOf(edtWeight.getText().toString());
                customer.phoneNumber = edtPhone.getText().toString();
                customer.addressStart = edtDeparture.getText().toString();
                customer.address = edtDestination.getText().toString();
                customer.name = mAuth.getCurrentUser().getEmail();

                //mDatabase.child("Customers").child(String.valueOf(i++)).setValue(customer);
                
            }
        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               handlingCreateImage();
            }
        });
    }

    private void handlingCreateImage() {
        SelectImageDialog dialogSelectImage = new SelectImageDialog(this,
                MapsActivity.this);
        dialogSelectImage.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imgPhoto.setBackgroundResource(0);
                    imgPhoto.setImageBitmap(photo);
                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();
                    Uri selectedImage = data.getData();
                    imgPhoto.setBackgroundResource(0);
                    imgPhoto.setImageURI(selectedImage);
                }
                break;
            default:
                break;
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }



}
