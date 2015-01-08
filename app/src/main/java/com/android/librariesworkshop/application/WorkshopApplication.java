package com.android.librariesworkshop.application;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.librariesworkshop.BuildConfig;
import com.android.librariesworkshop.BusEvent;
import com.android.librariesworkshop.IOUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import timber.log.Timber;

public class WorkshopApplication extends Application {

    public static Bus bus;

    public static GlobalOttoListener globalOttoListener;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus();
        globalOttoListener = new GlobalOttoListener();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private class GlobalOttoListener {
        private GlobalOttoListener() {
            bus.register(this);
        }

        @Subscribe
        public void onRetrofitError(BusEvent.RetrofitFailureEvent event) {
            // Display an error toast to the user
            String message;
            if (event.getError() != null) {
                try {
                    message = IOUtils.toString(event.getError().getResponse().getBody().in());
                } catch (Exception e1) {
                    message = "Server Error";
                }
            } else {
                message = "Server Error";
            }
            if (!TextUtils.isEmpty(message)) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "HTTP " + event.getError().getResponse().getStatus() + " Error", Toast.LENGTH_LONG).show();
            }
        }

    }
}
