package pl.beerlurk.beerlurk.dto.geocode;

import java.util.List;

import lombok.Value;

@Value
public final class ResultsWrapper {

    List<Result> results;
}
