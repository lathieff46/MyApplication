package com.artqueen.logicuniversitystationerysystem.Employee.Data;

import com.artqueen.logicuniversitystationerysystem.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by student on 01/03/15.
 */
public class Department extends HashMap<String,String>{
    final static String baseUrl = "http://10.10.1.144/Logic";

    public Department(String departmentid, String departmentName,String departmentHeadId,String collectionPoint,
                      String repname,String headterm){
        put("departmentid", departmentid);
        put("departmentName",departmentName);
        put("departmentHeadid", departmentHeadId);
        put("collectionpoint",collectionPoint);
        put("repname",repname);
        put("headterm",headterm);
    }

    public static Department getDepartment(String deptId) {
        Department p = null;
        try {
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/Service.svc/Department/%s", baseUrl, deptId));
            if(a!=null) {
                p = new Department(a.getString("DepartmentID"), a.getString("DepartmentName"), a.getString("DepartmentHeadID"), a.getString("CollectionPoint"), a.getString("DepartmentRepName"), a.getString("DepartmentHeadTerm"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}
