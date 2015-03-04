package com.artqueen.logicuniversitystationerysystem.Representative.Data;

import com.artqueen.logicuniversitystationerysystem.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shaikmdashiq on 3/3/15.
 */
public class DisbursementDetails extends HashMap<String,String> {
    final static String baseUrl = "http://10.10.1.144/Logic";

    public DisbursementDetails(String disbursementID, String ItemID,String qty,String pendingAmount,String disbursementDetailsId) {
        put("disbursementID", disbursementID);
        put("itemId", ItemID);
        put("quantity",qty);
        put("pendingAmount", pendingAmount);
        put("disbursementDetailsId",disbursementDetailsId);
    }


    public static List<DisbursementDetails> list(String disbursementID) {
        List<DisbursementDetails> list = new ArrayList<DisbursementDetails>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/Service.svc/DisbursementDetails/%s", baseUrl, disbursementID));
            if(a!=null) {
                for (int i = 0; i < a.length(); i++) {
                    JSONObject c = a.getJSONObject(i);
                    list.add(new DisbursementDetails(c.getString("DisbursementID"),
                            c.getString("ItemID"),
                            c.getString("Qty"),
                            c.getString("PendingAmount"), c.getString("DisbursementDetailsID")));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}