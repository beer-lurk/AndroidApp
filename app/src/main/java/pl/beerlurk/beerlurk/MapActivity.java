package pl.beerlurk.beerlurk;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
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

    private static final long LOCATION_REFRESH_TIME = 100;
    private static final float LOCATION_REFRESH_DISTANCE = 3.0F;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions position = new MarkerOptions().title("My location").position(latLng);
                mapFragment.getMap().addMarker(position);
            }
        });
        mapFragment.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //TODO: return my location
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));

        doCall(map);
    }

    private void doCall(final GoogleMap map) {
        Location myLocation = new Location("my");
        myLocation.setLatitude(52.2237465);
        myLocation.setLongitude(20.9476116);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MatrixApi matrixApi = adapter.create(MatrixApi.class);
        GeocodeApi geocodeApi = adapter.create(GeocodeApi.class);
        new BeerService(new BeerApi() {
            @Override
            public Observable<BeerLocationsWrapper> call(String beerName) {
                return Observable.just(Factory.create());
            }
        }, matrixApi, geocodeApi).call("beer", myLocation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DistancedBeerLocation>>() {
                    @Override
                    public void call(List<DistancedBeerLocation> distancedBeerLocations) {
                        Log.e("tag", "count: " + distancedBeerLocations.size());
                        Log.e("tag", distancedBeerLocations + "");
                        for (DistancedBeerLocation l : distancedBeerLocations) {
                            map.addMarker(new MarkerOptions().position(new LatLng(l.getLocation().getLatitude(), l.getLocation().getLongitude())));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("tag", "error", throwable);
                    }
                });
    }
}
