package pl.beerlurk.beerlurk.dto;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Data
@RequiredArgsConstructor
public final class DistancedBeerLocation implements Parcelable {

    final Integer distance;
    final String distanceFormatted;
    Location location;
    final BeerLocation beerLocation;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.distance);
        dest.writeString(this.distanceFormatted);
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.beerLocation, flags);
    }

    private DistancedBeerLocation(Parcel in) {
        this.distance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.distanceFormatted = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.beerLocation = in.readParcelable(BeerLocation.class.getClassLoader());
    }

    public static final Parcelable.Creator<DistancedBeerLocation> CREATOR = new Parcelable.Creator<DistancedBeerLocation>() {
        public DistancedBeerLocation createFromParcel(Parcel source) {
            return new DistancedBeerLocation(source);
        }

        public DistancedBeerLocation[] newArray(int size) {
            return new DistancedBeerLocation[size];
        }
    };
}
