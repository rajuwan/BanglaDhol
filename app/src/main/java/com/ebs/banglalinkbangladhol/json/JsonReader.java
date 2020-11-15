package com.ebs.banglalinkbangladhol.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ebs.banglalinkbangladhol.bean.Product;
import com.ebs.banglalinkbangladhol.util.TagName;

public class JsonReader {

	public static List<Product> getHome(String json) throws JSONException {

		List<Product> products = new ArrayList<Product>();

		JSONArray jsonArray = new JSONArray(json);

		Product product;
		for (int i = 0; i < jsonArray.length(); i++) {

			product = new Product();

			JSONObject productObj = jsonArray.getJSONObject(i);
			product.setAlbumcode(productObj.getString(TagName.KEY_CODE));
			product.setName(productObj.getString(TagName.KEY_NAME));
			product.setArtist(productObj.getString(TagName.KEY_ARTIST));
			//product.setLabelname(productObj.getString(TagName.KEY_PUBLISHER));
			product.setSongs(productObj.getInt(TagName.KEY_COUNT));
			product.setImg_url(productObj.getString(TagName.KEY_COVER_URL));
			product.setCatcode(productObj.getString(TagName.KEY_CAT_CODE));

			products.add(product);
		}
		return products;
	}
}
