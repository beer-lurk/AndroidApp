package pl.beerlurk.beerlurk.service;

import pl.beerlurk.beerlurk.dto.BeerLocationsWrapper;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface BeerApi {

    @GET("/find/{beer_name}")
    Observable<BeerLocationsWrapper> call(@Path("beer_name") String beerName);
}
