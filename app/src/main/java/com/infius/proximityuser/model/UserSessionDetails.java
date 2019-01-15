package com.infius.proximityuser.model;

import java.util.ArrayList;

public class UserSessionDetails implements DataModel {
    private String id;
    private String userId;
    private String name;
    private String email;
    private String mobile;
    private String countryCode;
    private String status;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getStatus() {
        return status;
    }

    public String getPrimaryRole() {
        return primaryRole;
    }

    public String getPrimaryZone() {
        return primaryZone;
    }

    public String getSite() {
        return site;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    private String primaryRole;
    private String primaryZone;
    private String site;
    private ArrayList<String> roles;
    private String profileImageUrl;
    private String thumbnailUrl;
}
