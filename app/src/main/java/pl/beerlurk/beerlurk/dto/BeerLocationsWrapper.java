package pl.beerlurk.beerlurk.dto;

import java.util.List;

import lombok.Value;

@Value
public final class BeerLocationsWrapper {

    List<BeerLocation> beerLocations;
}
