package com.ebs.banglalinkbangladhol.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ebs.banglalinkbangladhol.bean.SubProduct;
import com.ebs.banglalinkbangladhol.util.SUBTAG;

public class SubJsonReader {

	public static List<SubProduct> getHome(JSONObject jsonObject)
			throws JSONException {
		List<SubProduct> products = new ArrayList<SubProduct>();

		JSONArray jsonArray = jsonObject.getJSONArray(SUBTAG.TAG_PRODUCTS);
		SubProduct product;
		for (int i = 0; i < jsonArray.length(); i++) {

			product = new SubProduct();

			JSONObject productObj = jsonArray.getJSONObject(i);
			product.setId(productObj.getInt(SUBTAG.KEY_ID));
			product.setName(productObj.getString(SUBTAG.KEY_NAME));
			product.setPack(productObj.getString(SUBTAG.KEY_PACK));
			product.setText(productObj.getString(SUBTAG.KEY_TEXT));
			product.setRegMsg(productObj.getString(SUBTAG.KEY_MSG));
			products.add(product);
		}
		return products;
	}

}
