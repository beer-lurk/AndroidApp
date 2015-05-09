package pl.beerlurk.beerlurk.dto;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public final class BeerLocation implements Parcelable {

    String addressWithName;
    String description;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addressWithName);
        dest.writeString(this.description);
    }

    private BeerLocation(Parcel in) {
        this.addressWithName = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<BeerLocation> CREATOR = new Parcelable.Creator<BeerLocation>() {
        public BeerLocation createFromParcel(Parcel source) {
            return new BeerLocation(source);
        }

        public BeerLocation[] newArray(int size) {
            return new BeerLocation[size];
        }
    };
}
