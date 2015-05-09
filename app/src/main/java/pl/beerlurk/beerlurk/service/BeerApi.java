package pl.beerlurk.beerlurk.service;

import pl.beerlurk.beerlurk.dto.BeerLocationsWrapper;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface BeerApi {

    @GET("/")
    Observable<BeerLocationsWrapper> call();
}
