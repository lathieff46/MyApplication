package com.artqueen.logicuniversitystationerysystem;

/**
 * Created by shaikmdashiq on 28/2/15.
 */
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Items extends HashMap<String, String> {
    final static String baseUrl = "http://10.10.1.144/Logic";

    public Items(String itemid, String category, String description, String reorderlevel,String reorderqty, String unitofmeasure) {
        put("itemId", itemid);
        put("category", category);
        put("description", description);
        put("reorderLevel", reorderlevel);
        put("reorderQty",reorderqty);
        put("unitOfMeasure",unitofmeasure);
        put("qty","1");
          }

    public void saveQty(String number)
    {
        put("qty",number);
    }


    public static List<Items> list(String category) {
        List<Items> list = new ArrayList<Items>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/Service.svc/Item/%s", baseUrl,category));
            if(a!=null) {
                for (int i = 0; i < a.length(); i++) {
                    JSONObject c = a.getJSONObject(i);
                    list.add(new Items(c.getString("ItemId"),
                            c.getString("Category"),
                            c.getString("Description"),
                            c.getString("ReorderLevel"), c.getString("ReorderQty"), c.getString("UnitOfMeasure")));
                }
            }
        } catch (Exception e) {
        }
        return list;
    }
}