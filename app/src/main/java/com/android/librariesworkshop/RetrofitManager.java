package com.android.librariesworkshop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Headers;
import timber.log.Timber;

public class RetrofitManager {

    private static final Gson gson = new GsonBuilder().create();

    private static RestAdapter ADAPTER = new RestAdapter.Builder()
            .setEndpoint("http://www.mbta.com/uploadedfiles")
            .setConverter(new GsonConverter(gson))
            .setLog(new RestAdapter.Log() {
                @Override
                public void log(String message) {
                    Timber.d(message);
                }
            })
            .setLogLevel(RestAdapter.LogLevel.NONE)
            .build();

    private static MyService MY_SERVICE = ADAPTER.create(MyService.class);

    public static MyService getService() {
        return MY_SERVICE;
    }

    public interface MyService {

//        @GET("/entry")
//        void getSomething(@Query("id") int id, Callback<ResponseModel> callback);

//        @POST("/entry/add")
//        void postSomething(@Query("first_name") String first, @Query("last_name") String last, @Query("age") int age, Callback<ResponseModel> callback);

        @Headers({"Content-Type: text/plain"})
        @GET("/feed_info.txt")
        void getUpdateDate(Callback<Response> callback);

        @GET("/MBTA_GTFS.zip")
        void getGtfsFile(Callback<Response> callback);
    }

}
