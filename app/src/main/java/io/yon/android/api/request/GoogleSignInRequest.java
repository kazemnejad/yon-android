package io.yon.android.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by amirhosein on 9/5/2017 AD.
 */

public class GoogleSignInRequest {
    private String idToken;

    public GoogleSignInRequest(String idToken) {
        this.idToken = idToken;
    }

    @JsonProperty("id_token")
    public String getIdToken() {
        return idToken;
    }
}
