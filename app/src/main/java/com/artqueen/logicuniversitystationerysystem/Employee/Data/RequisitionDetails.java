package com.artqueen.logicuniversitystationerysystem.Employee.Data;

import com.artqueen.logicuniversitystationerysystem.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shaikmdashiq on 2/3/15.
 */
public class RequisitionDetails extends HashMap<String, String> {
    final static String baseUrl = "http://10.10.1.144/Logic";

    public RequisitionDetails(String itemid, String qty, String RequisitionDetailId, String RequisitionId) {
        put("itemId", itemid);
        put("qty", qty);
        put("requisitionDetailId", RequisitionDetailId);
        put("requisitionId", RequisitionId);
    }

    private void saveDesc(String des)
    {
        put("itemDesc",des);
    }
    public static List<RequisitionDetails> list(String requisitionId) {
        List<RequisitionDetails> list = new ArrayList<RequisitionDetails>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/Service.svc/RequisitionDetail/%s", baseUrl, requisitionId));
            if(a!=null) {
                for (int i = 0; i < a.length(); i++) {
                    JSONObject c = a.getJSONObject(i);
                    RequisitionDetails r=new RequisitionDetails(c.getString("ItemID"),
                            c.getString("Qty"),
                            c.getString("RequisitionDetailID"),
                            c.getString("RequisitionID"));
                    list.add(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteRequisitionDetail(Items a,int RequisitionID) {
        try {
            JSONParser.getJSONFromUrl(String.format("%s/Service.svc/DeleteRequisitionDetail/%s/%s", baseUrl, RequisitionID, a.get("itemId")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteRequisitionallDetial(int RequisitionID)
    {
        try {
            JSONParser.getJSONFromUrl(String.format("%s/Service.svc/DeleteRequisitionallDetail/%s", baseUrl, RequisitionID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}