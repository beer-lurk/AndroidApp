package pl.beerlurk.beerlurk;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import pl.beerlurk.beerlurk.dto.DistancedBeerLocation;
import pl.beerlurk.beerlurk.service.BeerApi;
import pl.beerlurk.beerlurk.service.BeerService;
import pl.beerlurk.beerlurk.service.GeocodeApi;
import pl.beerlurk.beerlurk.service.MatrixApi;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    public List<DistancedBeerLocation> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.main_list);
        final TextView emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setText("Try to search for something");
        listView.setEmptyView(emptyView);
        EditText editText = (EditText) findViewById(R.id.edit);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String search = v.getText().toString().trim();
                if (search.length() > 0) {
                    v.setHint("Last search: " + search);
                    v.setText(null);
                    listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1));
                    emptyView.setText("Searching...");
                    doCall(search);
                    return true;
                }
                return false;
            }
        });
        EventBus.getDefault().register(this);
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

    private void doCall(String search) {
        Location myLocation = new Location("my");
        myLocation.setLatitude(52.22037939);
        myLocation.setLongitude(21.0086596);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        RestAdapter googleAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RestAdapter beerAdapter = new RestAdapter.Builder()
                .setEndpoint("http://beer-lurk.herokuapp.com")
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MatrixApi matrixApi = googleAdapter.create(MatrixApi.class);
        GeocodeApi geocodeApi = googleAdapter.create(GeocodeApi.class);
        BeerApi beerApi = beerAdapter.create(BeerApi.class);
        new BeerService(beerApi, matrixApi, geocodeApi)
                .call(search, myLocation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DistancedBeerLocation>>() {

                    @Override
                    public void call(List<DistancedBeerLocation> distancedBeerLocations) {
                        Log.e("tag", "count: " + distancedBeerLocations.size());
                        Log.e("tag", distancedBeerLocations + "");
                        TextView emptyView = (TextView) findViewById(R.id.empty_view);
                        emptyView.setText("Found nothing.");
                        data = distancedBeerLocations;
                        showList();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("tag", "error", throwable);
                        TextView emptyView = (TextView) findViewById(R.id.empty_view);
                        emptyView.setText("Search failed.");
                    }
                });
    }

    private void showList() {
        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(new MyAdapter(data));
    }
}
