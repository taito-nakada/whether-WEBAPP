package design;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ForecastServlet
 */
@WebServlet("/ForecastServlet")
public class ForecastServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForecastServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IndexForecast fc = new IndexForecast();
		FavoritesBean fb = new FavoritesBean();
		RequestDispatcher dispatcher;
		String strLat = request.getParameter("lat");
		String strLng = request.getParameter("lng");
		String action = request.getParameter("action");
		String name = request.getParameter("name");
		
		double lat=0,lng=0;
		
		if(strLat == null || strLng == null) {
			fc.setErrorMessage("マップから座標を入力してください。");
			dispatcher = request.getRequestDispatcher("index.jsp");
		}else if(strLat.equals("") || strLng.equals("")){
			fc.setErrorMessage("マップから座標を入力してください。");
			dispatcher = request.getRequestDispatcher("index.jsp");
		}else {
			try {
				lat = Double.parseDouble(strLat);
				lng = Double.parseDouble(strLng);
				fc.setLat(lat);
				fc.setLng(lng);
				
				dispatcher = request.getRequestDispatcher("daily-forecast.jsp");
				
			}catch(NumberFormatException e) {
				//文字が入力された場合
				fc.setErrorMessage("無効な座標です。");
				dispatcher = request.getRequestDispatcher("index.jsp");	
			}
		}
		if(action == "subscribe") {
			if(name == null) {
				name="";
			}
			fb.setName(name);
			fb.setLatitude(lat);
			fb.setLongitude(lng);
			fb.insertRecord();
		}else if(action=="delete") {
			fb.setName(name);
			fb.deleteRecord();
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
