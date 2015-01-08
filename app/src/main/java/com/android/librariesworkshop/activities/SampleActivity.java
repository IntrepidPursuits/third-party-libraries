package com.android.librariesworkshop.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.librariesworkshop.R;
import com.android.librariesworkshop.fragments.FirstFragment;
import com.android.librariesworkshop.fragments.SecondFragment;


public class SampleActivity extends ActionBarActivity {

    private FirstFragment firstFragment;
    private SecondFragment secondFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        firstFragment = FirstFragment.newInstance("", "");
        secondFragment = SecondFragment.newInstance(0);

        getFragmentManager().beginTransaction().add(R.id.frame_1, firstFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.frame_2, secondFragment).commit();
    }

    public void plusOne() {
        secondFragment.addOne();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
