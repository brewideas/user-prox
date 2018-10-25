package com.infius.proximityuser.model;

import java.util.ArrayList;

public class GuestHistoryModel implements DataModel {

    String message;
    String status;
    ArrayList<PrimaryGuest> data;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<PrimaryGuest> getGuestsList() {
        return data;
    }
}
