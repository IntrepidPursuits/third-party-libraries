package com.android.librariesworkshop;

import retrofit.RetrofitError;

public class BusEvent {

    //TODO: Add an event that will be your argument in an Otto post


    public static class RetrofitFailureEvent extends BusEvent {
        private RetrofitError error;

        public RetrofitFailureEvent(RetrofitError error) {
            this.error = error;
        }

        public RetrofitError getError() {
            return error;
        }

    }

    public static class FragmentSampleEvent extends BusEvent {
        public FragmentSampleEvent() {
        }
    }
}
