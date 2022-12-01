<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="design.IndexForecast"%>
<%@ page import="design.FavoritesBean"%>
<!-- たいとの変更後にimportする -->
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>ウェジャーニー</title>
<link rel="stylesheet" href="normalize.css" />
<link rel="stylesheet" href="index.css" />
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
  href="https://fonts.googleapis.com/css2?family=Inter:wght@300;600&display=swap"
  rel="stylesheet" />
<link rel="stylesheet"
  href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" />
</head>
<body>
  <%
  IndexForecast fc = (IndexForecast) request.getAttribute("forecast");
  FavoritesBean fb = new FavoritesBean();
  double latitude = fc.getLat();
  double longitude = fc.getLng();
  
  String[] forecastData = fc.getForecastOfFavorite();
  //forecastData = fc.getDailyForecast();
  String formErrorMsg = forecastData[3];
  String bgClassName;//背景のグラデーションを設定するためのclass-name

  switch (forecastData[1]) {
  	case "Clear" :
  		bgClassName = "bg-sunny";
  		break;
  	case "Clouds" :
  		bgClassName = "bg-cloudy";
  		break;
  	case "Rain" :
  		bgClassName = "bg-rainy";
  		break;
  	case "Snow" :
  		bgClassName = "bg-rainy";
  		break;
  	case "ThunderStorm" :
  		bgClassName = "bg-rainy";
  		break;
  	default :
  		bgClassName = "bg-rainy";
  }

  java.util.ArrayList<FavoritesBean> favoritesPlaceList = fb.getUserRecords();
  %>
  <header>
    <div class="header-title-container">
      <svg class="sun-icon" xmlns="http://www.w3.org/2000/svg"
        width="48" height="48" fill="currentColor"
        class="bi bi-cloud-sun-fill" viewBox="0 0 16 16">
            <path
          d="M11.473 11a4.5 4.5 0 0 0-8.72-.99A3 3 0 0 0 3 16h8.5a2.5 2.5 0 0 0 0-5h-.027z" />
            <path
          d="M10.5 1.5a.5.5 0 0 0-1 0v1a.5.5 0 0 0 1 0v-1zm3.743 1.964a.5.5 0 1 0-.707-.707l-.708.707a.5.5 0 0 0 .708.708l.707-.708zm-7.779-.707a.5.5 0 0 0-.707.707l.707.708a.5.5 0 1 0 .708-.708l-.708-.707zm1.734 3.374a2 2 0 1 1 3.296 2.198c.199.281.372.582.516.898a3 3 0 1 0-4.84-3.225c.352.011.696.055 1.028.129zm4.484 4.074c.6.215 1.125.59 1.522 1.072a.5.5 0 0 0 .039-.742l-.707-.707a.5.5 0 0 0-.854.377zM14.5 6.5a.5.5 0 0 0 0 1h1a.5.5 0 0 0 0-1h-1z" />
          </svg>
      <a href="http://192.168.7.8/webapp-group08/"><h1
          class="header-heading">うぇじゃーにー</h1></a>
    </div>

    <div class="header-menu-container">

      <input type="checkbox" id="menu-btn-check" /> <label
        for="menu-btn-check" class="menu-btn"><span></span></label>
      <div class="menu-content">
        <h2>お気に入りスポット一覧</h2>
        <%
        if (favoritesPlaceList.size() <= 0) {
        %>
        <p>お気に入りスポットが登録されていません。</p>
        <%
        } else {
        %>
        <ul>

          <%
          for (int i = 0; i < favoritesPlaceList.size(); i++) {
          	String name = favoritesPlaceList.get(i).getName();
          	double lat = favoritesPlaceList.get(i).getLatitude();
          	double lng = favoritesPlaceList.get(i).getLongitude();
          %>
          <li>
            <p>
            <h1><%=name%></h1> <span><%=favoritesPlaceList.get(i).getLatitude()%>,
              <%=favoritesPlaceList.get(i).getLongitude()%></span> <a
            href=<%="http://192.168.7.8/webapp-group08/ForecastServlet?lat=" + String.valueOf(lat) + "&lng="
		+ String.valueOf(lng)%>>この場所の天気を見る</a>
            </p> <a
            href=<%="http://192.168.7.8/webapp-group08/ForecastServlet?action=delete&name=" + name%>>削除する</a>
          </li>
          <%
          }
          }
          %>
        </ul>
      </div>
    </div>
  </header>
  <div class="content">
    <div class="location-selector">
      <div>
        <h2>天気を知りたい場所を選択してください</h2>
      </div>
      <div id="map"></div>
      <div class="location-confirm-container">
        <div>
          <form action=ForecastServlet class="location-selector-form">
            <p style="color: red; margin-block: 5px;"><%=formErrorMsg%></p>
            <dl>
              <dt>lat</dt>
              <dd>
                <input type="text" name="lat" id="lat" value=<%=latitude %>
                  required="required" readonly>
              </dd>
              <dt>lng</dt>
              <dd>
                <input type="text" name="lng" id="lng" value=<%=longitude %>
                  required="required" readonly>
              </dd>
            </dl>
            <button type="submit" class="location-submit animoPulse"
              required="required">天気予報を見る</button>
          </form>
        </div>
      </div>
    </div>
    
    <div class="weather-container">
      <!--左の要素と高さを揃えるためのスペーサー-->
      <h2 class="spacer">天気予報が知りたい場所を選択してください</h2>
      <div class="weather-wrapper">
        <div class="weather-glass-box">
          <div class="weather-contents-container">
            <div class="weather-contents">
              <div>
                <h2><%=favoritesPlaceList.get(0).getName()%></h2>
                <p><%=favoritesPlaceList.get(0).getLatitude()%>,
                  <%=favoritesPlaceList.get(0).getLongitude()%></p>
                <h1 class="weather-temperture"><%=forecastData[0]%>℃
                </h1>
                <p class="weather-status"><%=forecastData[1]%></p>
              </div>
            </div>
          </div>
        </div>
        <!--雲を配置するための要素-->
        <div class="weather-glass-box-fake <%=bgClassName%>">
          <div class="weather-contents-container-fake">
            <div class="weather-contents-fake">
              <div>
                <h2><%=favoritesPlaceList.get(0).getName()%></h2>
                <p><%=favoritesPlaceList.get(0).getLatitude()%>,
                  <%=favoritesPlaceList.get(0).getLongitude()%></p>
                <h1><%=forecastData[0]%>℃
                </h1>
                <p><%=forecastData[1]%></p>
              </div>
              <div class="cloud-container-small">
                <img id="cloud-small"
                  src="assets/images/cloud-small.png" />
              </div>
            </div>
            <div class="cloud-container-big">
              <img id="cloud-big" src="assets/images/cloud-big.png" />
            </div>
          </div>
        </div>
      </div>
      <div class="speak-button-spacer"></div>
      <div class="speak-button-wrapper"><button class="location-submit animoPulse" onclick=<%="location.href='http://localhost:8080/webapp-group08/?action=speak'"%>>詳しい天気を聞く</button></div>
    </div>
    
  </div>
  
  <footer>
    <div class="header-title-container">
      <svg class="sun-icon" xmlns="http://www.w3.org/2000/svg"
        width="48" height="48" fill="currentColor"
        class="bi bi-cloud-sun-fill" viewBox="0 0 16 16">
            <path
          d="M11.473 11a4.5 4.5 0 0 0-8.72-.99A3 3 0 0 0 3 16h8.5a2.5 2.5 0 0 0 0-5h-.027z" />
            <path
          d="M10.5 1.5a.5.5 0 0 0-1 0v1a.5.5 0 0 0 1 0v-1zm3.743 1.964a.5.5 0 1 0-.707-.707l-.708.707a.5.5 0 0 0 .708.708l.707-.708zm-7.779-.707a.5.5 0 0 0-.707.707l.707.708a.5.5 0 1 0 .708-.708l-.708-.707zm1.734 3.374a2 2 0 1 1 3.296 2.198c.199.281.372.582.516.898a3 3 0 1 0-4.84-3.225c.352.011.696.055 1.028.129zm4.484 4.074c.6.215 1.125.59 1.522 1.072a.5.5 0 0 0 .039-.742l-.707-.707a.5.5 0 0 0-.854.377zM14.5 6.5a.5.5 0 0 0 0 1h1a.5.5 0 0 0 0-1h-1z" />
          </svg>
      <h1 class="header-heading">うぇじゃーにー</h1>
    </div>
    <p>NITTC Enginnering Design 2022. Group8</p>
    <p>Rei Kato, Yamato Sakuma, Taito Nakada, Ryo Matsubara</p>

  </footer>

  <!-- 必要なときだけ使う-->
  <script>
			function getClickLatLng(lat_lng, map) {
				document.getElementById('lat').value = lat_lng.lat();
				document.getElementById('lng').value = lat_lng.lng();

				var marker = new google.maps.Marker({
					position : lat_lng,
					map : map
				});

				map.panTo(lat_lng);
			}
			function initMap() {
				var latlng = new google.maps.LatLng(35.10397372325245,
						137.14882459016576) //中心の緯度, 経度
				var map = new google.maps.Map(document.getElementById('map'), {
					zoom : 14, //ズームの調整
					center : latlng, //上で設定した中心
				})

				map.addListener('click', function(e) {
					getClickLatLng(e.latLng, map);
				});

			}
		</script>
  <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBIGMFIzSrivxMATu9CKg4ORmpLZwb_wYA&callback=initMap"></script>
  <!---->
</body>
</html>
