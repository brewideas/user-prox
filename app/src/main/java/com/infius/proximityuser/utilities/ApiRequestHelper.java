package com.infius.proximityuser.utilities;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.infius.proximityuser.model.AccessTokenModel;
import com.infius.proximityuser.model.DataModel;
import com.infius.proximityuser.model.GuestHistoryModel;
import com.infius.proximityuser.model.InvitationModel;
import com.infius.proximityuser.network.GetRequest;
import com.infius.proximityuser.network.PostRequest;
import com.infius.proximityuser.network.VolleyManager;

import java.util.HashMap;

public class ApiRequestHelper {

    public static void requestGuestList(Context context, String param, Response.Listener<DataModel> listener, Response.ErrorListener errorListener) {
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath("http://34.231.195.192:9090/services/proximity/api/guest/list");
        builder.appendQueryParameter("param", "HISTORY");
//        String url = builder.build().toString();

        String url = "http://34.231.195.192:9090/services/proximity/api/guest/list?param=" + param;
        GetRequest request = new GetRequest(Request.Method.GET, url, listener, errorListener, new GuestHistoryModel(), null, null);
        VolleyManager.getRequestQueue(context).add(request);
    }

    public static void requestInvitation(Context context, String requestBody, Response.Listener<DataModel> listener, Response.ErrorListener errorListener) {
        String url = "http://34.231.195.192:9090/services/proximity/api/guest";
        HashMap<String, String> header = new HashMap<>();
        header.put("content-type", "application/json");
        PostRequest request = new PostRequest(url, listener, errorListener, new InvitationModel(), header, requestBody);
        VolleyManager.getRequestQueue(context).add(request);
    }

    public static void requestSignup(Context context, String requestBody, Response.Listener<DataModel> listener, Response.ErrorListener errorListener) {
        String url = "http://34.231.195.192:9090/services/proximity/api/account/resident";
        HashMap<String, String> header = new HashMap<>();
        header.put("content-type", "application/json");
        PostRequest request = new PostRequest(url, listener, errorListener, new InvitationModel(), header, requestBody);
        VolleyManager.getRequestQueue(context).add(request);
    }

    public static void requestSignin(Context context, String userId, String password, Response.Listener<DataModel> listener, Response.ErrorListener errorListener) {
        String url = "http://proximity:proximitysecret@34.231.195.192:9090/services/proximity/oauth/token?grant_type=password&username=" + userId + "&password=" + password;
        HashMap<String, String> header = new HashMap<>();
        header.put("content-type", "application/json");
        PostRequest request = new PostRequest(url, listener, errorListener, new AccessTokenModel(), header, null);
        VolleyManager.getRequestQueue(context).add(request);
    }
}
