package com.example.bo.smarthome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import java.net.MalformedURLException;
import java.net.URL;

public class HouseWeather extends Fragment {

    String value, min, max;
    Bitmap bm;
    View gui;
    HousesettingDetail housesetting;
    public HouseWeather(){}
    public HouseWeather(HousesettingDetail housesettingDetail){

        housesetting=housesettingDetail;
    }
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);


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
           //     URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
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
                        if (xpp.getName().equals("temperature")) {
                            value = xpp.getAttributeValue(null, "value");
                            // Thread.sleep(1000);
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            // Thread.sleep(1000);
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            //Thread.sleep(1000);
                            publishProgress(75);

                        } else if (xpp.getName().equals("weather")) {

                            String icon = xpp.getAttributeValue(null, "icon");

                            if (!fileExist(icon + ".png")) {

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
                            // Thread.sleep(1000);
                            publishProgress(100);

                        }


                    }
                    xpp.next();
                }

            } catch (Exception me) {
                Log.e("AsyncTask", "Malformed URL:" + me.getMessage());
            }

            return in;
        }

        public void onProgressUpdate(Integer... progress) {
            ProgressBar progressBar = (ProgressBar)gui.findViewById(R.id.house_progressBar);
            progressBar.setVisibility(View.VISIBLE);

            progressBar.setProgress(progress[0]);


        }

        public void onPostExecute(String work) {

            ImageView imageView = (ImageView) gui.findViewById(R.id.house_imageview);
            imageView.setImageBitmap(bm);

            TextView tx = (TextView) gui.findViewById(R.id.house_current_temp);
            tx.setText("Current Temperature is : " + value);

            ProgressBar progressBar = (ProgressBar) gui.findViewById(R.id.house_progressBar);
            progressBar.setVisibility(View.INVISIBLE);


            TextView tx2 = (TextView) gui.findViewById(R.id.house_min_temp);
            tx2.setText("\nMin Temperature is : " + min);

            TextView tx3 = (TextView) gui.findViewById(R.id.house_max_temp);
            tx3.setText("\nMax Temperature is : " + max);
        }


        public boolean fileExist(String filename) {

            File file =getActivity().getBaseContext().getFileStreamPath(filename);
            return file.exists();
        }


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

        public Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

    }

}
