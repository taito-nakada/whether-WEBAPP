package design;


import java.io.IOException;

import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


public class WeeklyForecast {
	String[][] Item = new String[7][5]; // [0]最低気温 [1]最高気温 [2]状態 [3]アイコン [4]エラーメッセージ
	String[][] Item1 = new String[7][5];
	String[][] Item2 = new String[7][5];

	public String[][] getWeeklyForecast_OpenWeatherMap(double lat, double lon) throws IOException { // 引数はGoogleMapから取得した緯度経度

		URL url = new URL(getURL_Weathermap(lat, lon));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		int responseCode = connection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {

			InputStreamReader isr = new InputStreamReader(connection.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String json = br.readLine();
//          System.out.println(json); //Test

			ObjectMapper mapper = new ObjectMapper();

			// 最初に得られる情報の前半は必要ないので、文字列検索してカットする
			int BeginIndexdaily = json.indexOf("\"daily\"");
			String ShortenedJson = json.substring(BeginIndexdaily); // 7days Remaining

			// lastIndexOf()関数で後ろから文字検索し、7日目から順に気象情報を読み取る
			String[] DayJson = new String[7];
			int lastindex = ShortenedJson.length();

			for (int i = 6; i >= 0; i--) {
				int BeginIndexdt = ShortenedJson.lastIndexOf("\"dt\"") - 1;
				DayJson[i] = ShortenedJson.substring(BeginIndexdt, lastindex); // i日目のデータの抜き出し
				ShortenedJson = ShortenedJson.substring(0, BeginIndexdt);
				lastindex = ShortenedJson.length();
			}

			for (int i = 0; i < Item.length; i++) {
				JsonNode node = mapper.readTree(DayJson[i]);

				String WeatherList = (node.get("weather").toString());
				String Newjson = WeatherList.substring(1, WeatherList.length() - 1);
				// 余分な配列鍵かっこの削除
				JsonNode Weathernode = mapper.readTree(Newjson);
				// "状態"用の新しいJsonNode

				Item[i][0] = node.get("temp").get("min").toString(); // 最低気温 - 例 22.33
				Item[i][1] = node.get("temp").get("max").toString(); // 最高気温 - 例 18.1
				String QuotationMain = Weathernode.get("main").toString(); // 状態の両端についている余分なダブルクオーテーションを削除するための一時的な変数
				Item[i][2] = QuotationMain.substring(1, QuotationMain.length() - 1); // 状態 - 例 Rain
				String QuotationIcon = Weathernode.get("icon").toString(); // 同上、アイコンのダブルクオーテーション
				Item[i][3] = QuotationIcon.substring(1, QuotationIcon.length() - 1); // アイコン - 例 10d
				Item[i][4] = ""; // エラーメッセージ

			}
		} else { // サーバーが応答しなかった場合、[4]にエラーメッセージを入れる
			for (int i = 0; i < Item.length; i++) {
				Item[i][4] = "RESPONSE_TIME_OUT";
			}

		}
		return Item;
	}

	public String[][] getWeeklyForecast_Kisyocho(double lat, double lon) throws IOException {

		// https://www.mgo-tec.com/blog-entry-jp-weather01.html#title02https://www.mgo-tec.com/blog-entry-jp-weather01.html#title02
		// sankouLink
		// https://www.t3a.jp/blog/web-develop/weather-code-list/https://www.t3a.jp/blog/web-develop/weather-code-list/
		// WeatherCode
		RaspberryMessage code = new RaspberryMessage();
		String Forecastcode = code.getForecastCode(lat, lon);
		URL url = new URL(getURL_Kisyocho(Forecastcode));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		int responseCode = connection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {

			InputStreamReader isr = new InputStreamReader(connection.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String json = br.readLine();
//          System.out.println(json); //Test

			ObjectMapper mapper = new ObjectMapper();

			// 最初に得られる情報の前半は必要ないので、文字列検索してカットする
			int TempsBeginIndexdaily1 = json.indexOf("\"areas\"");
			int WeatherBeginIndexdaily1 = json.indexOf("\"areas\"");

			for (int i = 0; i < 4; i++) {
				TempsBeginIndexdaily1 = json.indexOf("\"areas\"", TempsBeginIndexdaily1 + 5);
			}
			for (int i = 0; i < 3; i++) {
				WeatherBeginIndexdaily1 = json.indexOf("\"areas\"", WeatherBeginIndexdaily1 + 5);
			}

			String TempsShortenedJson1 = json.substring(TempsBeginIndexdaily1); // "areas"5個目までの情報をすべてカット
			int TempsBeginIndexdaily2 = TempsShortenedJson1.indexOf("]}"); // 気温の情報が終わる地点を探す
			String TempsShortenedJson2 = TempsShortenedJson1.substring(9, TempsBeginIndexdaily2 + 2); // 気温の情報が終わる地点以降の情報をすべてかっと

			String WeatherShortenedJson1 = json.substring(WeatherBeginIndexdaily1);
			int WeatherBeginIndexdaily2 = WeatherShortenedJson1.indexOf("]}");
			String WeatherShortenedJson2 = WeatherShortenedJson1.substring(9, WeatherBeginIndexdaily2 + 2);

			for (int i = 0; i < Item1.length; i++) {
				JsonNode nodeTemps = mapper.readTree(TempsShortenedJson2);
				JsonNode nodeWeather = mapper.readTree(WeatherShortenedJson2);

				Item1[i][0] = nodeTemps.get("tempsMin").get(i).toString(); // 最低気温
				Item1[i][1] = nodeTemps.get("tempsMax").get(i).toString(); // 最高気温
				Item1[i][2] = nodeWeather.get("weatherCodes").get(i).toString(); // 状態
				Item1[i][3] = ""; // アイコン
				Item1[i][4] = ""; // エラーメッセージ

			}
		} else { // サーバーが応答しなかった場合、[4]にエラーメッセージを入れる
			for (int i = 0; i < Item1.length; i++) {
				Item1[i][4] = "RESPONSE_TIME_OUT";
			}

		}
		return Item1;
	}

	public String[][] getWeeklyForecast_Meteo(double lat, double lon) throws IOException {

		// https://open-meteo.com/en/docs link
		// https://www.nodc.noaa.gov/archive/arc0021/0002199/1.1/data/0-data/HTML/WMO-CODE/WMO4677.HTM
		// weathercode
		URL url = new URL(getURL_Meteo(lat, lon));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {

			InputStreamReader isr = new InputStreamReader(connection.getInputStream());

			BufferedReader br = new BufferedReader(isr);

			String json = br.readLine();
//          System.out.println(json); //Test

			ObjectMapper mapper = new ObjectMapper();

			for (int i = 0; i < Item2.length; i++) {
				JsonNode node = mapper.readTree(json);

				Item2[i][0] = node.get("daily").get("temperature_2m_min").get(i).toString(); // 最低気温
				Item2[i][1] = node.get("daily").get("temperature_2m_max").get(i).toString(); // 最高気温
				Item2[i][2] = node.get("daily").get("weathercode").get(i).toString(); // 状態
				Item2[i][3] = ""; // アイコンは下記のcsvを読み込むところで代入
				Item2[i][4] = ""; // エラーメッセージ

			}
		} else { // サーバーが応答しなかった場合、[4]にエラーメッセージを入れる
			for (int i = 0; i < Item2.length; i++) {
				Item2[i][4] = "RESPONSE_TIME_OUT";
			}

		}



		return Item2;
	}

	public String getURL_Weathermap(double lat, double lon) {
		// URL(確認用)、東京
		// "https://api.openweathermap.org/data/2.5/onecall?lat=35.681236&lon=139.767125&units=metric&appid=2959f1a751626bac3348451e944ec7f3";
		return "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon
		        + "&units=metric&appid=2959f1a751626bac3348451e944ec7f3";
	}

	public String getURL_Kisyocho(String Forecastcode) {
		// URL(確認用)、東京 https://www.jma.go.jp/bosai/forecast/data/forecast/130000.json
		return "https://www.jma.go.jp/bosai/forecast/data/forecast/" + Forecastcode + ".json";
	}

	public String getURL_Meteo(double lat, double lon) {
		// URL(確認用)、東京
		// https://api.open-meteo.com/v1/forecast?latitude=35.68&longitude=139.76&daily=weathercode,temperature_2m_max,temperature_2m_min&timezone=Asia%2FTokyo
		// たいむぞーんどうしよｗ
		return "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon
		        + "&daily=weathercode,temperature_2m_max,temperature_2m_min&timezone=Asia%2FTokyo";
	}

}
