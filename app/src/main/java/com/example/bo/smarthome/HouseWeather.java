package com.example.bo.smarthome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bo on 2017-03-27.
 *
 *  This class is the database for the HouseSetting part
 */

public class HouseWeather extends Fragment {

    String value, min, max;
    Bitmap bm;
    View gui;
    HousesettingDetail housesetting;

    //constructor
    public HouseWeather(){}
    public HouseWeather(HousesettingDetail housesettingDetail){

        housesetting=housesettingDetail;
    }
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        ForecastQuery thread = new ForecastQuery();
        thread.execute();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
         gui = inflater.inflate(R.layout.activity_house_weather, null);

        return gui;
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>

    {
        protected String doInBackground(String... args) {

            String in = "";
            try {
               //instantiate a new URL to get the weather information
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric"); //("http://www.google.com/");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();


                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                Log.i("XML parsing:", "" + xpp.getEventType());
                int type;
                while ((type = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        //get the value of different tag, if it is temp, the get the current, max and min temp
                        if (xpp.getName().equals("temperature")) {
                            value = xpp.getAttributeValue(null, "value");
                            publishProgress(25);   //progress bar
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                    //if it is weather, then get the icon pf the weather information
                        } else if (xpp.getName().equals("weather")) {

                            String icon = xpp.getAttributeValue(null, "icon");
                            if (!fileExist(icon + ".png")) {
                                //URL to get the icon
                                URL ImageURL = new URL("http://openweathermap.org/img/w/" + icon + ".png");
                                Bitmap image = getImage(ImageURL);
                                FileOutputStream outputStream = getActivity().openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();

                                FileInputStream fis = null;
                                try {
                                    fis = getActivity().openFileInput(icon + ".png");

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                bm = BitmapFactory.decodeStream(fis);
                            } else {
                                FileInputStream fis = null;
                                try {
                                    fis = getActivity().openFileInput(icon + ".png");

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                bm = BitmapFactory.decodeStream(fis);

                            }

                            publishProgress(100); //after get all the information, progrogress bar done

                        }


                    }
                    xpp.next();
                }

            } catch (Exception me) {
                Log.e("AsyncTask", "Malformed URL:" + me.getMessage());
            }

            return in;
        }

        //set onProgressUpdate for progressBar visible
        public void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = (ProgressBar)gui.findViewById(R.id.house_progressBar);
            progressBar.setVisibility(View.VISIBLE);

            progressBar.setProgress(progress[0]);


        }

        //set the value of the icon and temp to the layout
        public void onPostExecute(String work) {
                //icon
            ImageView imageView = (ImageView) gui.findViewById(R.id.house_imageview);
            imageView.setImageBitmap(bm);
               //current temp
            TextView tx = (TextView) gui.findViewById(R.id.house_current_temp);
            tx.setText(getString(R.string.house_currenttemp) +" : " + value);
            //progressBar
            ProgressBar progressBar = (ProgressBar) gui.findViewById(R.id.house_progressBar);
            progressBar.setVisibility(View.INVISIBLE);

             //min temp
            TextView tx2 = (TextView) gui.findViewById(R.id.house_min_temp);
            tx2.setText(getString(R.string.house_mintemp)+" : " + min);
            //max temp
            TextView tx3 = (TextView) gui.findViewById(R.id.house_max_temp);
            tx3.setText(getString(R.string.house_maxtemp)+" : " + max);
        }

// to see if the file exist or not
        public boolean fileExist(String filename) {

            File file =getActivity().getBaseContext().getFileStreamPath(filename);
            return file.exists();
        }

// get the image
        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }



    }

}
