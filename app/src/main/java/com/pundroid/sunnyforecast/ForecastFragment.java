package com.pundroid.sunnyforecast;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pumba30 on 15.08.2015.
 */
public class ForecastFragment extends Fragment {
    public static final String TAG = ForecastFragment.class.getSimpleName();
    public static final String URL_FORECAST_FOR_SEVEN_DAYS = "http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7";
    private List<String> daysWeekList = new ArrayList<>();
    private String JSONString;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
        daysWeekList.add("Today - Sunny 88 - 63");
        daysWeekList.add("Tomorrow - Foggy 70 - 46");
        daysWeekList.add("Weds - Cloudy 72 - 63");
        daysWeekList.add("Thurs - Rainy 64 - 51");
        daysWeekList.add("Fri - foggy 70 - 45");
        daysWeekList.add("Sat - Sunny 88 - 63");
        daysWeekList.add("Sun - Sunny 88 - 63");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_item_forecast, R.id.list_item_forecast_textview, daysWeekList);

        ListView listViewForecast = (ListView) view.findViewById(R.id.listview_forecast);
        listViewForecast.setAdapter(adapter);

        new DownloadTask().execute(URL_FORECAST_FOR_SEVEN_DAYS);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.btn_refresh:

                return true;
       
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;

            String forecastJSONString = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuffer.append(line + "/n");
                }

                if (stringBuffer.length() == 0) {
                    return null;
                }


                forecastJSONString = stringBuffer.toString();
                Log.d(TAG, forecastJSONString);

            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return forecastJSONString;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONString = s;
            Log.d(TAG, "Our new query " + JSONString);
        }
    }

}
