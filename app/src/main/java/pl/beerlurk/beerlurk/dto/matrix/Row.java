package pl.beerlurk.beerlurk.dto.matrix;

import java.util.List;

import lombok.Value;

@Value
public final class Row {

    List<Element> elements;
}
