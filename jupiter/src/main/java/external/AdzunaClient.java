package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import entity.Item;
import entity.Item.*;
import entity.Item.ItemBuilder;

public class AdzunaClient {
	private static final String URL_TEMPLATE = 
			"https://api.adzuna.com/v1/api/jobs/us/search/"
			+ "1?app_id=%s&app_key=%s";
	
	private static final String APP_ID = "bfa0513a";
	private static final String APP_KEY = "092433561360b79effdca321ed0eaf68";
	
	public String getID() {
		return APP_ID;
	}
	
	public String getKEY() {
		return APP_KEY;
	}
	
	
	public List<Item> search(String id, String key) {
		if (id == null) {
			id = APP_ID;
		}
		if (key == null) {
			key = APP_KEY;
		}
		try {
			id = URLEncoder.encode(id, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			key = URLEncoder.encode(key, "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		String url = String.format(URL_TEMPLATE, id, key);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			CloseableHttpResponse response = httpClient.execute(new HttpGet(url));
			if (response.getStatusLine().getStatusCode() != 200) {
					return new ArrayList<Item>();
			}
			
			
			HttpEntity entity = response.getEntity();
			if (entity == null) {return new ArrayList<Item>();}
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
			
			StringBuilder responseBody = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBody.append(line);
			}
			JSONObject obj = new JSONObject(responseBody.toString());
			JSONArray arr = obj.getJSONArray("results");
			return getItemList(arr);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Item>();
	}
	
	public List<Item> getItemList(JSONArray arr) {
		List<Item> itemList = new ArrayList<>();
		for (int i = 0; i < arr.length(); ++i) {
			JSONObject obj = arr.getJSONObject(i);
			ItemBuilder builder = new ItemBuilder();
			builder.setItemId(getStringFieldOrEmpty(obj, "id"));
			builder.setName(getStringFieldOrEmpty(obj, "title"));
			builder.setAddress(obj.getJSONObject("location"));
			builder.setUrl(getStringFieldOrEmpty(obj, "redirect_url"));
			builder.setImageUrl(getStringFieldOrEmpty(obj, "created"));
			
			Item item = builder.build();
			itemList.add(item);
		}
		return itemList;
	}
	
	private String getStringFieldOrEmpty(JSONObject obj, String field) {
		return obj.isNull(field) ? "" : obj.getString(field);
	}
	
	
	public static void main(String[] args) {
		AdzunaClient client = new AdzunaClient();
		List<Item> itemList = client.search(APP_ID, APP_KEY);
		for (Item i : itemList) {
			JSONObject jObj = i.toJSONObject();
			System.out.println(jObj);
		}
	}
	
	
}
