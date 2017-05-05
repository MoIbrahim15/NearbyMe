package com.mohamedibrahim.nearbyme.utils;

import android.content.Context;

import com.mohamedibrahim.nearbyme.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Mohamed Ibrahim
 * on 5/5/2017.
 */

public class NetworkUtils {

    public static URL buildUrl(Context context, String locationParam) {
        String BASE_URL = context.getString(R.string.main_url);
        String base_param = "client_id=" + context.getResources().getString(R.string.client_id)
                + "&client_secret=" + context.getResources().getString(R.string.client_secret)
                + "&v=" + context.getResources().getString(R.string.api_version) + "&";

        URL url = null;
        try {
            url = new URL(BASE_URL.concat(base_param).concat(locationParam));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000); //TIMEOUT connections :)
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
