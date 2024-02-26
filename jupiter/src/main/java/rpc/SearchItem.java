package rpc;


import java.io.IOException;

import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.MySQLConnection;
import external.AdzunaClient;
import entity.Item;

/**
 * Servlet implementation class SearchItem
 */
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		HttpSession session = request.getSession(false);
//		if (session == null) {
//			response.setStatus(403);
//			return;
//		}
		
		String userId = request.getParameter("user_id");
		
		String apiId = request.getParameter("app_id");
		String apiKey = request.getParameter("app_key");
		String position = request.getParameter("what");
		String location = request.getParameter("where");
		
		AdzunaClient client = new AdzunaClient();
		
//		RpcHelper.writeItemList(response, client.search(client.getID(), client.getKEY()));
		
//		response.setContentType("application/json");
//		PrintWriter writer = response.getWriter();
		
		// Since our project should project dynamic services to users, 
		// it should be able to return different results based on different parameters from user input. 
		// Update the code in doGet() method to make the code check username parameter from user request.
		
		// Now update your doGet() method again to return a JSONArray 
		// format data in response.
		
//		JSONArray array = new JSONArray();
//		
//		try {
//			array.put(new JSONObject().put("username", "abcd"));
//			array.put(new JSONObject().put("username", "1234"));
//		} catch(JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		writer.print(array);
//		RpcHelper.writeJsonArray(response, array);
		
		List<Item> items = client.search(apiId, apiKey, position, location);
		
		MySQLConnection connection = new MySQLConnection();
		Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);
		connection.close();
		
		JSONArray array = new JSONArray();
		for (Item item : items) {
			JSONObject obj = item.toJSONObject();
			obj.put("favorite", favoritedItemIds.contains(item.getItemId()));
			array.put(obj);
		}
		RpcHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
