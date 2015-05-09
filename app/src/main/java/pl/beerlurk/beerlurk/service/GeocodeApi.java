package pl.beerlurk.beerlurk.service;

import pl.beerlurk.beerlurk.dto.geocode.ResultsWrapper;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface GeocodeApi {

    @GET("/maps/api/geocode/json")
    Observable<ResultsWrapper> call(@Query("address") String address);
}
