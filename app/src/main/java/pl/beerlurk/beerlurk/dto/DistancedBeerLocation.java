package pl.beerlurk.beerlurk.dto;

import android.location.Location;

import lombok.Data;
import lombok.Value;

@Data
public final class DistancedBeerLocation {

    final Integer distance;
    Location location;
    final BeerLocation beerLocation;
}
