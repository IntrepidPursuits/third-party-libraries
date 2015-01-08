package com.android.librariesworkshop;

import retrofit.RetrofitError;

public class BusEvent {

    public static class MyEvent extends BusEvent {
        private ResponseModel response;

        public MyEvent(ResponseModel response) {
            this.response = response;
        }

        public ResponseModel getResponse() {
            return response;
        }
    }

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
