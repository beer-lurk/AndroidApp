package pl.beerlurk.beerlurk.service;

import pl.beerlurk.beerlurk.dto.matrix.MatrixData;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface MatrixApi {

    @GET("/maps/api/distancematrix/json")
    Observable<MatrixData> call(@Query("origins") String myLocation, @Query("destinations") String destinations);
}
