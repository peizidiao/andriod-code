package mg.studio.weatherappdesign;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.String;


import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    private static final String[] states={"nonUpdate","alreadyUpdate"};
    private static final String TAG = "MainActivity";
    public String State=states[0];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view) {
        ConnectivityManager connManager = (ConnectivityManager) this
                .getSystemService(CONNECTIVITY_SERVICE);
        // 获取代表联网状态的NetWorkInfo对象
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        // 获取当前的网络连接是否可用
        if (null == networkInfo) {
            Toast.makeText(this, "The current network connection is unavailable", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "当前的网络连接不可用");
        } else {
            boolean available = networkInfo.isAvailable();
            if (available) {
                Log.i(TAG, "当前的网络连接可用");
                if(State.equals(states[0])){
                    new DownloadUpdate().execute();
                }
                else if(State.equals(states[1])){
                    Toast.makeText(this, "Has been updated, do not update it again", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i(TAG, "当前的网络连接不可用");
                Toast.makeText(this, "The current network connection is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getSpecifiedDayAfter(Date specifiedDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(specifiedDay);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime())+" 12:00:00";
        return dayAfter;
    }

    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "https://mpianatra.com/Courses/forecast.json";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("TAG", line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed

            //Get the current datetime, I simulate today as 2017-01-30 21:00:00
            boolean flag=false;
            DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String nowaday="2017-01-30 22:00:00";

            Date currDate=null;
            try{
                currDate=df.parse(nowaday);
            }
            catch (ParseException e){
                e.printStackTrace();
            }

            //create  date array and weeksum array
            String[] weeksum =new String[5];
            String[] dates=new String[5];

            Date newDate=currDate;
            for(int i=0;i<dates.length;i++){
                if(i==0){
                    dates[i]=nowaday;
                    weeksum[i]=getWeekOfDate(currDate);
                }
                else{
                    String dt =getSpecifiedDayAfter(newDate);
                    try{
                        newDate=df.parse(dt);
                    }
                    catch (ParseException e){
                        e.printStackTrace();
                    }
                    dates[i]=dt;
                    weeksum[i]=getWeekOfDate(newDate);
                }
            }


            //get location temperature array and weather array
            String location=null;
            String[] temp=new String[5];
            String[] weatherMain=new String[5];

            Log.d(TAG, temperature);
            try{
                JSONObject object=new JSONObject(temperature);
                String ListString=object.getString("list");
                JSONObject city=object.getJSONObject("city");
                location=city.getString("name");
                JSONArray List=new JSONArray(ListString);
                for(int i = 0, j = 0; i< List.length(); i++){
                    JSONObject listi=List.getJSONObject(i);
                    String dt_txt=listi.getString("dt_txt");
                    Date tempDate=df.parse(dt_txt);
                    if(!flag){
                        if(currDate.getTime()<=tempDate.getTime()) {
                            JSONObject main = listi.getJSONObject("main");
                            temp[0] = main.getString("temp");
                            JSONArray weather = listi.getJSONArray("weather");
                            weatherMain[0] = weather.getJSONObject(0).getString("main");
                            flag = true;
                        }
                    }
                    else{
                        if(j<4){
                            if(dates[j+1].equals(dt_txt)){
                                JSONObject main = listi.getJSONObject("main");
                                temp[j+1] = main.getString("temp");
                                JSONArray weather=listi.getJSONArray("weather");
                                weatherMain[j+1]=weather.getJSONObject(0).getString("main");
                                j++;
                            }
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            //change location
            ((TextView) findViewById(R.id.tv_location)).setText(location);

            //change the day of the week today
            ((TextView) findViewById(R.id.Today)).setText(weeksum[0]);

            //change the day of the week in the future
            ((TextView) findViewById(R.id.fuDay1)).setText(weeksum[1]);
            ((TextView) findViewById(R.id.fuDay2)).setText(weeksum[2]);
            ((TextView) findViewById(R.id.fuDay3)).setText(weeksum[3]);
            ((TextView) findViewById(R.id.fuDay4)).setText(weeksum[4]);

            //change temperature
            double[] t=new double[5];
            for(int i=0;i<5;i++){
                t[i]=Double.parseDouble(temp[i]);
                t[i]-=273.15;
                t[i]  =  (double)(Math.round(t[i]*100))/100;
            }
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(String.valueOf(t[0]));
            ((TextView) findViewById(R.id.fuDaytmp1)).setText(String.valueOf(t[1])+"℃");
            ((TextView) findViewById(R.id.fuDaytmp2)).setText(String.valueOf(t[2])+"℃");
            ((TextView) findViewById(R.id.fuDaytmp3)).setText(String.valueOf(t[3])+"℃");
            ((TextView) findViewById(R.id.fuDaytmp4)).setText(String.valueOf(t[4])+"℃");

            //change icons today
            if(weatherMain[0].equals("Rain")){
                ((ImageView) findViewById(R.id.img_weather_condition)).setImageDrawable(getResources().getDrawable(R.drawable.rainy_small));
            }
            else if(weatherMain[0].equals("Clouds")){
                ((ImageView) findViewById(R.id.img_weather_condition)).setImageDrawable(getResources().getDrawable(R.drawable.partly_sunny_small));
            }
            else if(weatherMain[0].equals("Snow")){
                ((ImageView) findViewById(R.id.img_weather_condition)).setImageDrawable(getResources().getDrawable(R.drawable.windy_small));
            }
            else if(weatherMain[0].equals("Clear")){
                ((ImageView) findViewById(R.id.img_weather_condition)).setImageDrawable(getResources().getDrawable(R.drawable.sunny_small));
            }

            //change future icons
            if(weatherMain[1].equals("Rain")){
                ((ImageView) findViewById(R.id.icon1)).setImageDrawable(getResources().getDrawable(R.drawable.rainy_small));
            }
            else if(weatherMain[1].equals("Clouds")){
                ((ImageView) findViewById(R.id.icon1)).setImageDrawable(getResources().getDrawable(R.drawable.partly_sunny_small));
            }
            else if(weatherMain[1].equals("Snow")){
                ((ImageView) findViewById(R.id.icon1)).setImageDrawable(getResources().getDrawable(R.drawable.windy_small));
            }
            else if(weatherMain[1].equals("Clear")){
                ((ImageView) findViewById(R.id.icon1)).setImageDrawable(getResources().getDrawable(R.drawable.sunny_small));
            }


            if(weatherMain[2].equals("Rain")){
                ((ImageView) findViewById(R.id.icon2)).setImageDrawable(getResources().getDrawable(R.drawable.rainy_small));
            }
            else if(weatherMain[2].equals("Clouds")){
                ((ImageView) findViewById(R.id.icon2)).setImageDrawable(getResources().getDrawable(R.drawable.partly_sunny_small));
            }
            else if(weatherMain[2].equals("Snow")){
                ((ImageView) findViewById(R.id.icon2)).setImageDrawable(getResources().getDrawable(R.drawable.windy_small));
            }
            else if(weatherMain[2].equals("Clear")){
                ((ImageView) findViewById(R.id.icon2)).setImageDrawable(getResources().getDrawable(R.drawable.sunny_small));
            }


            if(weatherMain[3].equals("Rain")){
                ((ImageView) findViewById(R.id.icon3)).setImageDrawable(getResources().getDrawable(R.drawable.rainy_small));
            }
            else if(weatherMain[3].equals("Clouds")){
                ((ImageView) findViewById(R.id.icon3)).setImageDrawable(getResources().getDrawable(R.drawable.partly_sunny_small));
            }
            else if(weatherMain[3].equals("Snow")){
                ((ImageView) findViewById(R.id.icon3)).setImageDrawable(getResources().getDrawable(R.drawable.windy_small));
            }
            else if(weatherMain[3].equals("Clear")){
                ((ImageView) findViewById(R.id.icon3)).setImageDrawable(getResources().getDrawable(R.drawable.sunny_small));
            }


            if(weatherMain[4].equals("Rain")){
                ((ImageView) findViewById(R.id.icon4)).setImageDrawable(getResources().getDrawable(R.drawable.rainy_small));
            }
            else if(weatherMain[4].equals("Clouds")){
                ((ImageView) findViewById(R.id.icon4)).setImageDrawable(getResources().getDrawable(R.drawable.partly_sunny_small));
            }
            else if(weatherMain[4].equals("Snow")){
                ((ImageView) findViewById(R.id.icon4)).setImageDrawable(getResources().getDrawable(R.drawable.windy_small));
            }
            else if(weatherMain[4].equals("Clear")){
                ((ImageView) findViewById(R.id.icon4)).setImageDrawable(getResources().getDrawable(R.drawable.sunny_small));
            }

            State=states[1];
        }
    }
}
