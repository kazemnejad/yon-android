package io.yon.android.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by amirhosein on 6/8/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicResponse {
    private int code;
    private String message;

    public BasicResponse() {}

    public int getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }
}
