package com.android.librariesworkshop.activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.librariesworkshop.BusEvent;
import com.android.librariesworkshop.IOUtils;
import com.android.librariesworkshop.R;
import com.android.librariesworkshop.ResponseModel;
import com.android.librariesworkshop.RetrofitManager;
import com.android.librariesworkshop.application.WorkshopApplication;
import com.squareup.otto.Subscribe;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;
import timber.log.Timber;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.id_num)
    EditText idNumber;

    @InjectViews({R.id.first_name, R.id.last_name, R.id.age})
    List<EditText> entryFields;

    @InjectView(R.id.text_1)
    TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        WorkshopApplication.bus.register(this);
    }

    @OnClick(R.id.get_button)
    void makeServerCall() {
        RetrofitManager.getService().getUpdateDate(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Timber.d("Worked!");
                InputStream in = null;
                try {
                    in = response.getBody().in();
                } catch (IOException e) {
                    e.printStackTrace();
                }
/*                byte[] buffer = new byte[1024];
                try {
                    in.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/


                String s = null;
                try {
                    s = IOUtils.toString(in);
                } catch (IOException e) {
                    Timber.e(e, "failed");
                }
                s.length();

            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error, "Failed");
            }
        });

//        String s = String.valueOf(idNumber.getText());
//        int id = TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s);

        /*if (id > 0) {
            RetrofitManager.getService().getSomething(id, new Callback<ResponseModel>() {
                @Override
                public void success(ResponseModel responseModel, Response response) {
                    WorkshopApplication.bus.post(new BusEvent.MyEvent(responseModel));
                }

                @Override
                public void failure(RetrofitError error) {
                    WorkshopApplication.bus.post(new BusEvent.RetrofitFailureEvent(error));
                }
            });
        }*/
    }

    @OnClick(R.id.post_button)
    void postInformation() {

        unzipGTFS(Environment.getExternalStorageDirectory().getAbsolutePath(), "gtfs.zip");

        if (true) {
            return;
        }

        RetrofitManager.getService().getGtfsFile(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Timber.d("Worked!");

                File f = downloadZipFile(response);

                unzipGTFS("/storage/emulated/0/", f.getName());

            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error, "Failed");
            }
        });

//        String firstName = String.valueOf(entryFields.get(0).getText());
//        String lastName = String.valueOf(entryFields.get(1).getText());
//        String s = String.valueOf(entryFields.get(2).getText());
//        int age = TextUtils.isEmpty(s) ? 0 : Integer.parseInt(s);

        /*if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && age > 0) {
            RetrofitManager.getService().postSomething(firstName, lastName, age, new Callback<ResponseModel>() {
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
        }*/
    }

    private File downloadZipFile(Response response) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        File f = new File(Environment.getExternalStorageDirectory(), "gtfs.zip");

        //this will be used to write the downloaded data into the file we created
        FileOutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TypedInput body = response.getBody();
        long totalSize = body.length();

        InputStream in = null;
        try {
            in = body.in();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //variable to store total downloaded bytes
        int downloadedSize = 0;

        //create a buffer...
        byte[] buffer = new byte[1024];
        int bufferLength = 0; //used to store a temporary size of the buffer

        //now, read through the input buffer and write the contents to the file
        try {
            while ((bufferLength = in.read(buffer)) > 0) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
                //this is where you would do something to report the prgress, like this maybe
                //                    updateProgress(downloadedSize, totalSize);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //close the output stream when done
        try {
            fileOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public boolean unzipGTFS(String path, String zipName) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + "/" + zipName);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + "/" + filename);
                    fmd.mkdirs();
                    continue;
                }

                File gtfsDirectory = new File(path + "/gtfs");
                if (!gtfsDirectory.exists()) {
                    gtfsDirectory.mkdir();
                }

                FileOutputStream fout = new FileOutputStream(gtfsDirectory.getAbsolutePath() + "/" + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPause() {
        WorkshopApplication.bus.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void response(BusEvent.MyEvent event) {
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();

        ResponseModel.User user = event.getResponse().getUser();
        response.setText("First: " + user.getFirstName() + "\nLast: " + user.getLastName() + "\nAge: " + user.getAge());
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
