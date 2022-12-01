<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="design.IndexForecast" %> 
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
      rel="stylesheet"
    />
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
    />
  </head>
  <body>
  <%
      IndexForecast fc=(IndexForecast)request.getAttribute("forecast");
    String[] forecastData = fc.getForecastOfFavorite();
    String bgClassName;//背景のグラデーションを設定するためのclass-name
    
    switch(forecastData[1]){
    case "Clear":
      bgClassName="bg-sunny";
      break;
    case "Clouds":
      bgClassName="bg-cloudy";
      break;
    case "Rain":
      bgClassName="bg-rainy";
      break;
    case "Snow":
      bgClassName="bg-rainy";
      break;
    case "ThunderStorm":
      bgClassName="bg-rainy";
      break;
     default:
       bgClassName="bg-rainy";
    }
  %>
    <div class="main">
      <div class="header">
        <div class="header-title-container">
          <svg
            class="sun-icon"
            xmlns="http://www.w3.org/2000/svg"
            width="48"
            height="48"
            fill="currentColor"
            class="bi bi-cloud-sun-fill"
            viewBox="0 0 16 16"
          >
            <path
              d="M11.473 11a4.5 4.5 0 0 0-8.72-.99A3 3 0 0 0 3 16h8.5a2.5 2.5 0 0 0 0-5h-.027z"
            />
            <path
              d="M10.5 1.5a.5.5 0 0 0-1 0v1a.5.5 0 0 0 1 0v-1zm3.743 1.964a.5.5 0 1 0-.707-.707l-.708.707a.5.5 0 0 0 .708.708l.707-.708zm-7.779-.707a.5.5 0 0 0-.707.707l.707.708a.5.5 0 1 0 .708-.708l-.708-.707zm1.734 3.374a2 2 0 1 1 3.296 2.198c.199.281.372.582.516.898a3 3 0 1 0-4.84-3.225c.352.011.696.055 1.028.129zm4.484 4.074c.6.215 1.125.59 1.522 1.072a.5.5 0 0 0 .039-.742l-.707-.707a.5.5 0 0 0-.854.377zM14.5 6.5a.5.5 0 0 0 0 1h1a.5.5 0 0 0 0-1h-1z"
            />
          </svg>
          <h1 class="header-heading">Weather App</h1>
        </div>
        <div class="header-menu-container">
          <div class="hamburger-menu">
            <input type="checkbox" id="menu-btn-check" />
            <label for="menu-btn-check" class="menu-btn"><span></span></label>
            
            <div class="menu-content">
              <li>
            </div>
          </div>
        </div>
      </div>
      <div class="content">
        <div class="weather-container">
          <!--左の要素と高さを揃えるためのスペーサー-->
          <div><h2>　　　　　　　　　　　　　　　　</h2></div>
          <div class="weather-wrapper">
            <div class="weather-glass-box">
              <div class="weather-contents-container">
                <div class="weather-contents">
                  <div>
                    <h2>予期せぬエラーが発生しました。</h2>
                    <h2>エラーの内容はこちらです</h2>
                    <h1 class="weather-temperture"><%=forecastData[5]%></h1>
                    <p class="weather-status"></p>
                  </div>
                </div>
              </div>
            </div>
            <!--雲を配置するための要素-->
            <div class="weather-glass-box-fake bg-rainy">
              <div class="weather-contents-container-fake">
                <div class="weather-contents-fake">
                  <div>
                    <h2>Toyota City</h2>
                    <h2></h2>
                    <h1>24℃</h1>
                    <p>Sunny</p>
                  </div>
                  <div class="cloud-container-small">
                    <img id="cloud-small" src="assets/images/cloud-small.png" />
                  </div>
                </div>
                <div class="cloud-container-big">
                  <img id="cloud-big" src="assets/images/cloud-big.png" />
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="forecast-error-box">
        <a href="/webapp-group08/">indexページに戻る</a>
        </div>
      </div>
    </div>
  </body>
</html>
