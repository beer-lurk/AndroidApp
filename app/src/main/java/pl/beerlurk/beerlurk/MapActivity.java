package pl.beerlurk.beerlurk;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import pl.beerlurk.beerlurk.dto.BeerLocationsWrapper;
import pl.beerlurk.beerlurk.dto.DistancedBeerLocation;
import pl.beerlurk.beerlurk.dto.Factory;
import pl.beerlurk.beerlurk.service.BeerApi;
import pl.beerlurk.beerlurk.service.BeerService;
import pl.beerlurk.beerlurk.service.GeocodeApi;
import pl.beerlurk.beerlurk.service.MatrixApi;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MapActivity extends Activity implements OnMapReadyCallback {

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        DistancedBeerLocation clickedLocation = getIntent().getParcelableExtra("clicked");
        LatLng center = new LatLng(clickedLocation.getLocation().getLatitude(), clickedLocation.getLocation().getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15));

        List<DistancedBeerLocation> all = getIntent().getParcelableArrayListExtra("all");
        for (DistancedBeerLocation l : all) {
            LatLng position = new LatLng(l.getLocation().getLatitude(), l.getLocation().getLongitude());
            map.addMarker(new MarkerOptions().position(position).title(l.getBeerLocation().getAddressWithName()));
        }
    }
}
