package pl.beerlurk.beerlurk;

import android.location.Location;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import rx.functions.Action1;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            doCall();
            return true;
        } else if (id == R.id.select_location) {
            startActivity(new Intent(this, MapActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void doCall() {
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
            public Observable<BeerLocationsWrapper> call() {
                return Observable.just(Factory.create());
            }
        }, matrixApi, geocodeApi).call("beer", myLocation).subscribe(new Action1<List<DistancedBeerLocation>>() {
            @Override
            public void call(List<DistancedBeerLocation> distancedBeerLocations) {
                Log.e("tag", "count: " + distancedBeerLocations.size());
                Log.e("tag", distancedBeerLocations + "");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("tag", "error", throwable);
            }
        });
    }
}
