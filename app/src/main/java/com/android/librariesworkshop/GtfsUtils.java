package com.android.librariesworkshop;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class GtfsUtils {

    private static String GTFS_FOLDER = "gtfs";

    public static boolean unzipGTFS(String path, String zipName) {
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
                // Name of file in ZIP
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + "/" + filename);
                    fmd.mkdirs();
                    continue;
                }

                File gtfsDirectory = new File(path + "/" + GTFS_FOLDER);
                if (!gtfsDirectory.exists()) {
                    gtfsDirectory.mkdir();
                }

                FileOutputStream fout = new FileOutputStream(gtfsDirectory.getAbsolutePath() + "/" + filename);

                // Write it
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

    public static File downloadZipFile(Response response) {
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
        int bufferLength; //used to store a temporary size of the buffer

        //now, read through the input buffer and write the contents to the file
        try {
            while ((bufferLength = in.read(buffer)) > 0) {
                //add the data in the buffer to the file in the file output stream (the file on the sd card
                fileOutput.write(buffer, 0, bufferLength);
                //add up the size so we know how much is downloaded
                downloadedSize += bufferLength;
                //this is where you would do something to report the progress, like this maybe
                updateProgress(downloadedSize, totalSize);

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

    public static void updateProgress(int downloadedSize, long totalSize) {

    }
}
