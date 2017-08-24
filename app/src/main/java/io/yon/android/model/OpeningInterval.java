package io.yon.android.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.parceler.Parcel;

/**
 * Created by amirhosein on 8/23/2017 AD.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpeningInterval extends Model {
    long start;
    long end;

    public OpeningInterval() {}

    public OpeningInterval(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
