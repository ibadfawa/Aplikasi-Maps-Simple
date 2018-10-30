package dev.id.bariscode.projectmapsapigoogle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

public class PlaceAutoCompleteActivity extends AppCompatActivity {

    // Deklarasi Variable
    private TextView tvPickUpFrom, tvDestLocation;
    private TextView tvPickUpAddr, tvPickUpLatLng, tvPickUpName;
    private TextView tvDestLocAddr, tvDestLocLatLng, tvDestLocName;

    public static final int PICK_UP = 0;
    public static final int DEST_LOC = 1;
    private static int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_auto_complete);

        getSupportActionBar().setTitle("Place Auto Complete");
        wigetInit();

        tvPickUpFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlacefromAutoComplete(PICK_UP);
            }
        });

        tvDestLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlacefromAutoComplete(DEST_LOC);
            }
        });
    }

    private void wigetInit() {
        tvPickUpFrom = findViewById(R.id.tvPickUpFrom);
        tvDestLocation = findViewById(R.id.tvDestLocation);
        tvPickUpAddr = findViewById(R.id.tvPickUpAddr);
        tvPickUpLatLng = findViewById(R.id.tvPickUpLatLng);
        tvPickUpName = findViewById(R.id.tvPickUpName);
        tvDestLocAddr = findViewById(R.id.tvDestLocAddr);
        tvDestLocLatLng = findViewById(R.id.tvDestLocLatLng);
        tvDestLocName = findViewById(R.id.tvDestLocName);
    }

    private void showPlacefromAutoComplete(int typeLocation) {
        REQUEST_CODE = typeLocation;

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("ID").build();
        try {
            // Intent untuk mengirim Implisit Intent
            Intent mIntent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(this);
            // jalankan intent impilist
            startActivityForResult(mIntent, REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace(); // cetak error
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace(); // cetak error
            // Display Toast
            Toast.makeText(this, "Layanan Play Services Tidak Tersedia", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Place placeLoc = PlaceAutocomplete.getPlace(this, data);

            if (placeLoc.isDataValid()) {

                Log.d("autoCompletePlace Data", placeLoc.toString());

                String placeAddress = placeLoc.getAddress().toString();
                LatLng placeLatLng = placeLoc.getLatLng();
                String placeName = placeLoc.getName().toString();

                switch (REQUEST_CODE) {
                    case PICK_UP:
                        tvPickUpFrom.setText(placeAddress);
                        tvPickUpAddr.setText("Location Address : " + placeAddress);
                        tvPickUpLatLng.setText("LatLang : " + placeLatLng.toString());
                        tvPickUpName.setText("Place Name : " + placeName);

                        break;
                    case DEST_LOC:
                        tvDestLocation.setText(placeAddress);
                        tvDestLocAddr.setText("Destination Address : " + placeAddress);
                        tvDestLocLatLng.setText("LatLang : " + placeLatLng.toString());
                        tvDestLocName.setText("Place Name : " + placeName);

                        break;
                }

            } else {
                Toast.makeText(this, "Invalid Place !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

