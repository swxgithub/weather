package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    static public int x = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DownloadUpdate().execute();
    }



    public void btnClick(View view) {
        new DownloadUpdate().execute();
        if(x != 0)
        Toast.makeText(getApplicationContext(), "天气已更新",Toast.LENGTH_SHORT).show();
    }

    public String weektra(String str)
    {
      switch(str)
      {
          case("星期一"):str = "MON";break;
          case("星期二"):str = "TUE";break;
          case("星期三"):str = "WED";break;
          case("星期四"):str = "THU";break;
          case("星期五"):str = "FRI";break;
          case("星期六"):str = "SAT";break;
          case("星期日"):str = "SUN";break;
      }
      return str;
    }

    public String GW(String str){
        str = str.substring(str.indexOf("高") + 3, str.indexOf("℃") - 2);
        return str;
    }
    public String DW(String str){
        str = str.substring(str.indexOf("低") + 3, str.indexOf("℃") - 2);
        return str;
    }



    public class DownloadUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://t.weather.sojson.com/api/weather/city/101010100";
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
                    buffer.append(line + "\n");

                   System.out.println(line + "\n");
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
        protected void onPostExecute(String buffer) {

           x = x+1;
            String date, t1;
            String week[] = new String[5];
            String type[] = new String[5];
            String gw[] = new String[5];
            String dw[] = new String[5];

            t1 = buffer.substring(buffer.indexOf("wendu") + 8, buffer.indexOf("ganmao") - 3);
            date = buffer.substring(buffer.indexOf("time") + 7, buffer.indexOf("time") + 17);

            JsonParser parser = new JsonParser();  //创建JSON解析器
            JsonObject object = (JsonObject) parser.parse(buffer);  //创建JsonObject对象

            JsonObject objectdata = object.get("data").getAsJsonObject();

            JsonArray array = objectdata.get("forecast").getAsJsonArray();    //得到为json的数组
            for (int i = 0; i <= 4; i++) {
                //System.out.println("---------------");
                JsonObject subObject = array.get(i).getAsJsonObject();
                week[i] = subObject.get("week").getAsString();
                type[i] = subObject.get("type").getAsString();
                gw[i] = subObject.get("high").getAsString();
                dw[i] = subObject.get("low").getAsString();
                gw[i] = GW(gw[i]);
                dw[i] = DW(dw[i]);
                //System.out.println(pic[i]);
                week[i] = weektra(week[i]);
            }

            ((TextView)findViewById(R.id.t2)).setText(gw[1]);
            ((TextView)findViewById(R.id.t3)).setText(dw[1]);
            ((TextView)findViewById(R.id.t4)).setText(gw[2]);
            ((TextView)findViewById(R.id.t5)).setText(dw[2]);
            ((TextView)findViewById(R.id.t6)).setText(gw[3]);
            ((TextView)findViewById(R.id.t7)).setText(dw[3]);
            ((TextView)findViewById(R.id.t8)).setText(gw[4]);
            ((TextView)findViewById(R.id.t9)).setText(dw[4]);

            ((TextView)findViewById(R.id.t1)).setText(t1);
            ((TextView)findViewById(R.id.tv_date)).setText(date);
            ((TextView)findViewById(R.id.zero)).setText(week[0]);
            ((TextView)findViewById(R.id.one)).setText(week[1]);
            ((TextView)findViewById(R.id.two)).setText(week[2]);
            ((TextView)findViewById(R.id.three)).setText(week[3]);
            ((TextView)findViewById(R.id.four)).setText(week[4]);
            switch(type[0])
            {
                case("晴"):((ImageView) findViewById(R.id.img1)).setImageResource(R.drawable.sunny_small);break;
                case("多云"):((ImageView) findViewById(R.id.img1)).setImageResource(R.drawable.partly_sunny_small);break;
                case("霾"):((ImageView) findViewById(R.id.img1)).setImageResource(R.drawable.windy_small);break;
                case("小雨"):((ImageView) findViewById(R.id.img1)).setImageResource(R.drawable.rainy_small);break;
            }
            switch(type[1])
            {
                case("晴"):((ImageView) findViewById(R.id.img2)).setImageResource(R.drawable.sunny_small);break;
                case("多云"):((ImageView) findViewById(R.id.img2)).setImageResource(R.drawable.partly_sunny_small);break;
                case("霾"):((ImageView) findViewById(R.id.img2)).setImageResource(R.drawable.windy_small);break;
                case("小雨"):((ImageView) findViewById(R.id.img2)).setImageResource(R.drawable.rainy_small);break;
            }
            switch(type[2])
            {
                case("晴"):((ImageView) findViewById(R.id.img3)).setImageResource(R.drawable.sunny_small);break;
                case("多云"):((ImageView) findViewById(R.id.img3)).setImageResource(R.drawable.partly_sunny_small);break;
                case("霾"):((ImageView) findViewById(R.id.img3)).setImageResource(R.drawable.windy_small);break;
                case("小雨"):((ImageView) findViewById(R.id.img3)).setImageResource(R.drawable.rainy_small);break;
            }
            switch(type[3])
            {
                case("晴"):((ImageView) findViewById(R.id.img4)).setImageResource(R.drawable.sunny_small);break;
                case("多云"):((ImageView) findViewById(R.id.img4)).setImageResource(R.drawable.partly_sunny_small);break;
                case("霾"):((ImageView) findViewById(R.id.img4)).setImageResource(R.drawable.windy_small);break;
                case("小雨"):((ImageView) findViewById(R.id.img4)).setImageResource(R.drawable.rainy_small);break;
            }
            switch(type[4])
            {
                case("晴"):((ImageView) findViewById(R.id.img5)).setImageResource(R.drawable.sunny_small);break;
                case("多云"):((ImageView) findViewById(R.id.img5)).setImageResource(R.drawable.partly_sunny_small);break;
                case("霾"):((ImageView) findViewById(R.id.img5)).setImageResource(R.drawable.windy_small);break;
                case("小雨"):((ImageView) findViewById(R.id.img5)).setImageResource(R.drawable.rainy_small);break;
            }
            //((ImageView) findViewById(R.id.img1)).setImageResource(R.drawable.sunny_small);
            //((TextView)findViewById(R.id.four)).setText(pic[3]);
            //((TextView)findViewById(R.id.t1)).setText(t1);



        }
    }
}
