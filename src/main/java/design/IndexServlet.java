package design;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/IndexServlet")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IndexForecast fc = new IndexForecast();
		FavoritesBean fb = new FavoritesBean();
		RaspberryMessage RM = new RaspberryMessage();
		String action = request.getParameter("action");
		java.util.ArrayList<FavoritesBean> favoritesList = fb.getUserRecords();
		
		fc.setPlace(favoritesList.get(0).getName());
		RequestDispatcher dispatcher;
		
		fc.setForecastOfFavorite(favoritesList.get(0).getLatitude(),favoritesList.get(0).getLongitude());
		//String[] forecastData = fc.fetchForecastOfFavorite();
		//
		//private String[] dailyForecast = new String[6];
		//setDailyForecast(getForecasstOfFavorite());
		
		//無事に取得できた場合はgetForecastOfFavorite()の返り値の配列の[5]に空文字列が入る
		String error = (fc.getForecastOfFavorite())[3];
		if(error=="") {
			dispatcher = request.getRequestDispatcher("index.jsp");
		}else{//空じゃなかった場合はerror.jspでエラー内容が出力できるように値をセットしてdispatchする
			fc.setErrorMessage(error);
			dispatcher = request.getRequestDispatcher("forecastError.jsp");
		}
		if(action=="speak") {
			URL url = new URL("http://192.168.7.28:3000/voice?msg="+ RM.getRaspberryMessage(favoritesList.get(0).getLatitude(),favoritesList.get(0).getLongitude()) );
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
		}
		
		request.setAttribute("forecast", fc);
		
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
