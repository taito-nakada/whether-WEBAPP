package design;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class IndexForecast {	//一日分の天気を表示する関数
	private String place;
	private double lat;
	private double lng;
	private String[] Item = new String[4];    //[0]気温 [1]状態 [2]アイコン [3]エラーメッセージ
	// URL(確認用)、東京 https://api.openweathermap.org/data/2.5/weather?lat=35.681236&lon=139.767125&units=metric&APPID=2959f1a751626bac3348451e944ec7f3
	public IndexForecast() {
		lat=0;
		lng=0;
	}
	
	public void setPlace(String name) {
		place = name;
	}
	public String getPlace() {
		return place;
	}
	public void setLat(double latitude) {
		lat = latitude;
	}
	public double getLat() {
		return lat;
	}
	public void setLng(double longitude) {
		lng = longitude;
	}
	public double getLng() {
		return lng;
	}
	
	public void setForecastOfFavorite(double lat, double lon) throws IOException {	//引数は都市名

		URL url = new URL(getURL(lat,lon));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		int responseCode = connection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {

			InputStreamReader isr = new InputStreamReader(connection.getInputStream());

			BufferedReader br = new BufferedReader(isr);


			String json = br.readLine();
//			System.out.println(json); //Test

			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(json);

			String WeatherList = (node.get("weather").toString());
			String Newjson = WeatherList.substring(1, WeatherList.length() - 1);
			//余分な配列鍵かっこの削除
			JsonNode Weathernode = mapper.readTree(Newjson);
			//"状態"用の新しいJsonNode


			Item[0] = node.get("main").get("temp").toString();					//気温 - 例 22.24 小数第二位 or 第一位までが代入される
			String QuotationMain = Weathernode.get("main").toString();			//状態の両端についている余分なダブルクオーテーションを削除するための一時的な変数
			Item[1] = QuotationMain.substring(1,QuotationMain.length() - 1);	//状態 - 例 Clouds
			String QuotationIcon = Weathernode.get("icon").toString();			//同上、アイコンのダブルクオーテーション
			Item[2] = QuotationIcon.substring(1, QuotationIcon.length() - 1);	//アイコン - 例 04n
			Item[3] = "";														//エラーメッセージ
		}
		else{	//サーバーが応答しなかった場合、[3]にエラーメッセージを入れる
			Item[3] = "RESPONSE_TIME_OUT";

		}
	}
	
	public String[] getForecastOfFavorite() {
		return Item;
	}

	public String getURL(double lat , double lon) throws IOException{
		return "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon="+ lon + "&units=metric&APPID=2959f1a751626bac3348451e944ec7f3";
	}
	public void setErrorMessage(String error){	//エラーメッセージを表示する関数
		Item[3] = error;
	}

}
