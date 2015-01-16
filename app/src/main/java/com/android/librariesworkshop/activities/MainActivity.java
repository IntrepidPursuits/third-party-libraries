package com.android.librariesworkshop.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.librariesworkshop.BusEvent;
import com.android.librariesworkshop.R;
import com.android.librariesworkshop.ResponseModel;
import com.android.librariesworkshop.RetrofitManager;
import com.android.librariesworkshop.application.WorkshopApplication;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    @InjectViews({R.id.first_name, R.id.last_name})
    List<EditText> entryFields;

    @InjectView(R.id.text_1)
    TextView response;

    @InjectView(R.id.avatar)
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.get_button)
    void postInformation() {
        String firstName = String.valueOf(entryFields.get(0).getText());
        String lastName = String.valueOf(entryFields.get(1).getText());
        String s = String.valueOf(entryFields.get(2).getText());
        int age = Integer.parseInt(s);

        if (firstName.length() > 0 || lastName.length() > 0 && age > 0) {
            RetrofitManager.getService().getUserInfo(firstName, lastName, new Callback<ResponseModel>() {
                @Override
                public void success(ResponseModel responseModel, Response response) {
                    WorkshopApplication.bus.post(new BusEvent.MyEvent(responseModel));
                }

                @Override
                public void failure(RetrofitError error) {
                    WorkshopApplication.bus.post(new BusEvent.RetrofitFailureEvent(error));
                }
            });
        } else {
            Toast.makeText(this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void response(BusEvent.MyEvent event) {
        ResponseModel.User user = event.getResponse().getUser();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        String avatarUrl = user.getAvatarUrl();
        Picasso.with(this).load(avatarUrl).fit().centerCrop().into(avatar);

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)) {
            response.setText("First: " + firstName + "\nLast: " + lastName + "\nAge: " + user.getAge());
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: Fields are null", Toast.LENGTH_SHORT).show();
        }
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
