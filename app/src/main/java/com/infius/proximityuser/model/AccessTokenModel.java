package com.infius.proximityuser.model;

public class AccessTokenModel implements DataModel {
    String access_token;
    String token_type;
    String refresh_token;
    String expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getExpires_in() {
        return expires_in;
    }
}
