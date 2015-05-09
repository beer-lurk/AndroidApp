package pl.beerlurk.beerlurk;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
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

public class MainActivity extends AppCompatActivity {

    public List<DistancedBeerLocation> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setEmptyView(findViewById(R.id.empty_view));
        EventBus.getDefault().register(this);
        doCall();
    }

    @SuppressWarnings("unused")
    public void onEvent(ShowOnMapClickEvent event) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("clicked", event.getDistancedBeerLocation());
        intent.putParcelableArrayListExtra("all", new ArrayList<>(data));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        }, matrixApi, geocodeApi).call("beer", myLocation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DistancedBeerLocation>>() {

                    @Override
                    public void call(List<DistancedBeerLocation> distancedBeerLocations) {
                        Log.e("tag", "count: " + distancedBeerLocations.size());
                        Log.e("tag", distancedBeerLocations + "");
                        data = distancedBeerLocations;
                        showList();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("tag", "error", throwable);
                    }
                });
    }

    private void showList() {
        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(new MyAdapter(data));
    }
}
