package com.artqueen.logicuniversitystationerysystem.Employee.Data;

import com.artqueen.logicuniversitystationerysystem.JSONParser;

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
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/Service.svc/GetLastRequisition/%s", baseUrl, eid));
            if(a!=null) {
                p = new Requisition(String.valueOf(a.getInt("RequisitionID")), a.getString("DepartmentName"), a.getString("EmployeeID"), a.getString("Status"), a.getString("Comments"), a.getString("ProcessStatus"), a.getString("Date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }


    public static void DeleteRequistion(int id)
    {
        try {
            JSONParser.getJSONFromUrl(String.format("%s/Service.svc/DeleteRequisition/%s", baseUrl, id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Requisition> list(String empId) {
        List<Requisition> list = new ArrayList<Requisition>();
        try {
            JSONArray a = JSONParser.getJSONArrayFromUrl(String.format("%s/Service.svc/Requisition/%s", baseUrl,empId));
            if(a!=null) {
                for (int i = 0; i < a.length(); i++) {
                    JSONObject c = a.getJSONObject(i);
                    list.add(new Requisition(c.getString("RequisitionID"),
                            c.getString("DepartmentName"),
                            c.getString("EmployeeID"),
                            c.getString("Status"), c.getString("Comments"), c.getString("ProcessStatus"),c.getString("Date")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
