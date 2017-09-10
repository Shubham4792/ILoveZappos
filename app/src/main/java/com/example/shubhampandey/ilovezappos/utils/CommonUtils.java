package com.example.shubhampandey.ilovezappos.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SHUBHAM PANDEY on 9/10/2017.
 */

public class CommonUtils {
    /**
     * Returns boolean stating if network is available
     *
     * @param context context called from
     * @return boolean stating network available or not
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Hit the api with the URL as the argument and retrieve response as JSONObject
     * @param apiURL URL for the API
     * @return JSONObject response
     */
    public static JSONObject getJSON(String apiURL){
        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder json = new StringBuilder(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();
            return new JSONObject(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hit the api with the URL as the argument and retrieve response as JSONObject
     * @param apiURL URL for the API
     * @return JSONObject response
     */
    public static JSONArray getJSON(String apiURL, String dummy){
        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder json = new StringBuilder(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();
            return new JSONArray(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
