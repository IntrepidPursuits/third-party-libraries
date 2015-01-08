package com.android.librariesworkshop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import timber.log.Timber;

public class RetrofitManager {

    private static final Gson gson = new GsonBuilder().create();

    private static RestAdapter ADAPTER = new RestAdapter.Builder()
            .setEndpoint("http://mockserver.com")
            .setConverter(new GsonConverter(gson))
            .setLog(new RestAdapter.Log() {
                @Override
                public void log(String message) {
                    Timber.d(message);
                }
            })
            .setClient(new MockServer())
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

    //TODO: Service

    private static MyService MY_SERVICE = ADAPTER.create(MyService.class);

    public static MyService getService() {
        return MY_SERVICE;
    }

    //TODO: Interface

    public interface MyService {

        //TODO: Make HTTP GET call to server, with a single query parameter

        //TODO: Make HTTP POST call to server, with 3 parameters

        /** look at {@link com.android.librariesworkshop.ResponseModel}
         */

        //TODO: Figure these out
        @GET("/entry")
        void getSomething(@Query("id") int id, Callback<ResponseModel> callback);

        @POST("/entry/add")
        void postSomething(@Query("first_name") String first, @Query("last_name") String last, @Query("age") int age, Callback<ResponseModel> callback);
    }

}
