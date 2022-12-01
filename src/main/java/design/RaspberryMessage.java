package design;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


public class RaspberryMessage {

    public String getForecastCode(double lat , double lon)throws IOException{  //lat lonを引数に本来使用する
        URL url = new URL(getURL_Forecastcode(lat, lon));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK) {
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String ForecastcodeJson = br.readLine();

            System.out.println(ForecastcodeJson);   //Test

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(ForecastcodeJson);

            String forecastcode = node.get("forecastcode").toString();
            forecastcode = forecastcode.substring(1,forecastcode.length()-1);
            return forecastcode;
        }
        else{   //サバ応答なしの場合
            return "RESPONSE_TIME_OUT";
        }
    }

    public String[] getDetailedPlaceName(double lat , double lon)throws IOException{  //lat lonを引数に本来使用する
        String[] Item= new String[3];   //[0]県名 [1]市名 [2]エラーメッセージ

        URL url = new URL(getURL_Forecastcode(lat, lon));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();

        if(responseCode == HttpURLConnection.HTTP_OK) {
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String ForecastcodeJson = br.readLine();

            System.out.println(ForecastcodeJson);   //Test

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(ForecastcodeJson);

            String PrefectureName = node.get("prefname").toString();
            PrefectureName = PrefectureName.substring(1,PrefectureName.length()-1); //ダブルクオーテーション削除

            String CityName = node.get("cityname").toString();
            CityName = CityName.substring(1,CityName.length() - 1);
            Item[0] = PrefectureName;
            Item[1] = CityName;
            Item[2] = "";
            return Item;

        }
        else{   //サバ応答なしの場合
            Item[2] = "RESPONSE_TIME_OUT";
            return Item;
        }
    }

    public String getRaspberryMessage(double lat, double lon) throws IOException {

        URL url = new URL(getURL_Weather(getForecastCode(lat, lon)));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();  //気象庁

        if (responseCode == HttpURLConnection.HTTP_OK ) {

            InputStreamReader isr = new InputStreamReader(connection.getInputStream());

            BufferedReader br = new BufferedReader(isr);

            String WeatherJson = br.readLine();

            System.out.println(WeatherJson); //Test

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(WeatherJson);

            String WeatherMessage = node.get("text").toString();

            return WeatherMessage;
        }
        else{
            return "RESPONSE_TIME_OUT";
        }
    }

    public String getURL_Forecastcode(double lat , double lon){
        //https://revgeo-forecastcode.herokuapp.com/lat=35.6895+lon=139.6917 確認用 東京都新宿区
        return "https://revgeo-forecastcode.herokuapp.com/lat="+lat+"+lon="+lon;
    }

    public String getURL_Weather(String ForecastCode){
        //https://www.jma.go.jp/bosai/forecast/data/overview_forecast/130000.json
        //https://www.jma.go.jp/bosai/forecast/data/overview_week/130000.json
        //上詳細、下概況。どちらを使う場合もコード変える必要なし
        return "https://www.jma.go.jp/bosai/forecast/data/overview_week/" + ForecastCode + ".json";
    }
}
