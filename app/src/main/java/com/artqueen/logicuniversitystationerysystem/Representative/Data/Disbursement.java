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
public class Disbursement extends HashMap<String,String> {
    final static String baseUrl = "http://10.10.1.144/Logic";

    public Disbursement(String disbursementID, String DepartmentId,String date,String comments,String clerkID,String status) {
        put("disbursementID", disbursementID);
        put("departmentID", DepartmentId);
        put("date",date);
        put("comments", comments);
        put("clerkID",clerkID);
        put("status",status);
    }


    public static List<Disbursement> list(String deptID) {
        List<Disbursement> list = new ArrayList<Disbursement>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/Service.svc/Disbursement/%s", baseUrl,deptID));
            if(a!=null) {
                for (int i = 0; i < a.length(); i++) {
                    JSONObject c = a.getJSONObject(i);
                    list.add(new Disbursement(c.getString("DisbursementID"),
                            c.getString("DepartmentID"),
                            c.getString("Date"),
                            c.getString("Comments"), c.getString("ClerkID"), c.getString("Status")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
