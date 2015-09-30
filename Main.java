import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Main {

	private static final String SHOPIFY_URL = "http://shopicruit.myshopify.com/products.json";
	
	public static void main (String[] args) {
		JSONObject json = null;

		try {
			json = new JSONObject(requestUrl(SHOPIFY_URL));
		} catch (Exception e){
			e.printStackTrace();
		}
		
		System.out.println ("Total cost: " + calculateCost(json));
	}
	
	// function to read in the url data as text
	private static String requestUrl(String urlString) throws Exception{
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1) {
				buffer.append(chars, 0, read);
			}
			return buffer.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	// function to calculate cost given a json object
	private static double calculateCost(JSONObject json) {
		// json object null check
		if (json == null) {
			return 0;
		}
		double cost = 0;
		try {
			JSONArray products = json.getJSONArray("products");
			// traverse each product for product type
			for (int i = 0; i < products.length(); i++) {
				JSONObject currentProduct = products.getJSONObject(i);
				if (currentProduct.get("product_type").toString().equals("Lamp") || currentProduct.get("product_type").toString().equals("Wallet")){
					JSONArray variants = currentProduct.getJSONArray("variants");
					// traverse current product for each variant
					for (int j = 0; j < variants.length(); j++) {
						cost += variants.getJSONObject(j).getDouble("price");
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cost;
	}
}
