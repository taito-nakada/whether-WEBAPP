package design;

import java.io.IOException;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection
import java.util.Calendar;
import java.util.Date;
@WebServlet("/WeeklyForecastServlet")

public static int Count=0;

public class WeeklyForecastServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeeklyForecastServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //  ラズベリーパイから送信された 文字列 の取得
    		Part part_txt = request.getPart("text_tag");
    		// Part からは InputStream　しか取れないので、以下の手順で String に変換 
    		InputStream is = part_txt.getInputStream();
    		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    		StringBuilder sb = new StringBuilder();
    		String line;
    		while( null != (line = reader.readLine()) ) {
    			sb.append(line);
    		}
    		String bangou = sb.toString();
    		int r = Integer.parseInt(String bangou);
    		ArrayList<FavoritesBean> list = (ArrayList<FavoritesBean>)request.getAttribute("FavoritesBeanList");
	    	
    		//raspiの日付を適応させるためにカウントの外部変数必要かも
    		
    		int t=Count;
    		if(1<=r&&r<=4) {
    			FavoritesBean sb = (FavoritesBean)list.get(r-1);
    			t=0;
    		}else if(r==5) {
    			int t-=1;
    		}else if(r==6) {
    			int t+=1;
    		}
    		Count=t;
    		public static void main(String[] args throws Exception) {
    			
    			Calendar cal = Calendar.getInstance();
    			cal.setTime(nowDate);
    			cal.add(Calendar.DAY_OF_MONTH,t);
    			
    			String strDate = new SimpleDateFormat("MM-dd",Locale.JP).format(cal.getTime());
    			
    			Weeklyforecast wf = new Weeklyforecast();
    			String[][] weekWeather = wf.getWeeklyForecast_OpenWeatherMap(sb.Latitude(),sb.Longitude());
    			String weather = weekWeather[t][2];
    			
    	    	URL url = new URL("https://reqres.in/api/users?spot="+sb.getName()+"&weather="+weather+"&day="+strDate);
    	    	URLConnection connection = url.openConnection();
    	    	
    	    	 //'HttpURLConnction' connection = (HttpURLConnection) url.openConnection();
    	    	 //connection.setRequestMethod("Get");
    	    	
    	    }
    }
}
        
        
       