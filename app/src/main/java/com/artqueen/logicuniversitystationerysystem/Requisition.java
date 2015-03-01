package com.artqueen.logicuniversitystationerysystem;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by student on 01/03/15.
 */
public class Requisition extends HashMap<String,String> {
    final static String baseUrl = "http://10.10.1.144/Logic";

    public Requisition(String requisitionID, String DepartmentName,String EmployeeID,String Status,String Comments,String ProcessStatus,String Date) {
        put("requisitionID", requisitionID);
        put("DepartmentName", DepartmentName);
        put("EmployeeID",EmployeeID);
        put("Status", Status);
        put("Comments",Comments);
        put("ProcessStatus",ProcessStatus);
        put("Date",Date);
    }

    public static Requisition GetLastRequisition(String eid) {
        Requisition p = null;
        try {
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/Service.svc/GetLastRequisition/%s", baseUrl,eid));
            if(a!=null) {
                p = new Requisition(String.valueOf(a.getInt("RequisitionID")), a.getString("DepartmentName"), a.getString("EmployeeID"), a.getString("Status"), a.getString("Comments"), a.getString("ProcessStatus"), a.getString("Date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public static List<String> list(String empId) {
        List<String> list = new ArrayList<String>();
        JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/Service.svc/Requisition/%s", baseUrl,empId));
        try {
            for (int i =0; i<a.length(); i++) {
                String b = a.getString(i);
                list.add(b);
            }
        } catch (Exception e) {
            Log.e("list", "JSONArray error");
        }
        return(list);
    }
}
