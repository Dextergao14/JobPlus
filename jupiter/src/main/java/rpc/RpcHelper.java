package rpc;

import java.util.List;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import entity.Item;
import entity.Item.ItemBuilder;


public class RpcHelper {
	// Writes a JSONArray to http response.
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException{
		response.setContentType("applictaion/json");
		response.getWriter().print(array);
	}
	
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(obj);
	}
	
	public static void writeItemList(HttpServletResponse response, List<Item> itemList) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(itemList);
	}
}
