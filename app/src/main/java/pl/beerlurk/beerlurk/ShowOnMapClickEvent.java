package pl.beerlurk.beerlurk;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import pl.beerlurk.beerlurk.dto.DistancedBeerLocation;

@Value
@EqualsAndHashCode
@ToString
public final class ShowOnMapClickEvent {

    DistancedBeerLocation distancedBeerLocation;
}
