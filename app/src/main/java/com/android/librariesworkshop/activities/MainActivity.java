package com.android.librariesworkshop.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.librariesworkshop.BusEvent;
import com.android.librariesworkshop.R;
import com.android.librariesworkshop.ResponseModel;
import com.android.librariesworkshop.RetrofitManager;
import com.android.librariesworkshop.application.WorkshopApplication;

import java.util.List;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.id_num)
    EditText idNumber;

    @InjectViews({R.id.first_name, R.id.last_name, R.id.age})
    List<EditText> entryFields;

    //TODO: Inject the TextView R.id.text_1, which displays the server response

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @OnClick(R.id.get_button)
    void makeServerCall() {
        String s = String.valueOf(idNumber.getText());
        int id = TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s);

        if (id > 0) {
            RetrofitManager.getService().getSomething(id, new Callback<ResponseModel>() {
                @Override
                public void success(ResponseModel responseModel, Response response) {
                    //TODO: Post an event that links w/the Subscribe method below (you need to add that as well)
                }

                @Override
                public void failure(RetrofitError error) {
                    WorkshopApplication.bus.post(new BusEvent.RetrofitFailureEvent(error));
                }
            });
        }
    }

    @OnClick(R.id.post_button)
    void postInformation() {
        String firstName = String.valueOf(entryFields.get(0).getText());
        String lastName = String.valueOf(entryFields.get(1).getText());
        String s = String.valueOf(entryFields.get(2).getText());
        int age = TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s);

        //TODO: call your retrofit post method
        /** {@link com.android.librariesworkshop.RetrofitManager}
         */

    }

    //TODO: Add a Subscribe method to display the response of your HTTP POST call


    @Override
    protected void onPause() {
        WorkshopApplication.bus.unregister(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SampleActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
