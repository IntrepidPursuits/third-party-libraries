package com.android.librariesworkshop;

import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import timber.log.Timber;

public class MockServer implements Client {

    @Override
    public Response execute(Request request) throws IOException {
        Uri uri = Uri.parse(request.getUrl());

        Timber.d("MOCK SERVER", "fetching uri: " + uri.toString());

        String responseString = "";
        String path = uri.getPath();
        int responseCode = 200;

        if (path.contains("/add")) {
            try {
                String id = uri.getQueryParameter("id");
                String firstName = uri.getQueryParameter("first_name");
                String lastName = uri.getQueryParameter("last_name");
                String age = uri.getQueryParameter("age");

                responseString = CannedResponses.getResponse(id, firstName, lastName, age);
            } catch (Exception e) {
                responseString = CannedResponses.FAILED;
            }
        } else if (path.contains("/entry")) {
            try {
                String id = uri.getQueryParameter("id");

                responseString = CannedResponses.getResponse(id, "Bill", "Jones", "24");
            } catch (Exception e) {
                responseString = CannedResponses.FAILED;
            }
        } else {
            responseCode = 400;
        }

        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("OkHttp-Sent-Millis", String.valueOf(System.currentTimeMillis())));

        return new Response(request.getUrl(), responseCode, "woop", headers, new TypedByteArray("application/json", responseString.getBytes()));
    }

    public static class CannedResponses {
        public static String GET_SUCCESFUL = "{\"success\"=true,\"message\"=\"entry successfully updated\"}";
        public static String FAILED = "{\"success\"=false,\"message\"=\"invalid parameters\"}";

        public static String getResponse(String id, String firstName, String lastName, String age) {
            return "{\"success\"=true,\"message\"=\"entry found\",\"user\"={\"id\"=" + id + ",\"first_name\"=\"" + firstName + "\",\"last_name\"=" + lastName + ",\"age\"=" + age + "}}";
        }

    }
}
