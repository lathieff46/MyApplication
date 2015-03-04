package com.artqueen.logicuniversitystationerysystem.Employee.Data;

import com.artqueen.logicuniversitystationerysystem.JSONParser;

import org.json.JSONObject;

import java.util.HashMap;

public class Users extends HashMap<String, String> {
    final static String baseUrl = "http://10.10.1.144/Logic";

    public Users(String userId, String userName,String userEmail,String userDeptId,String userPhoto,String userPassword,String userRoleID) {
        put("userId", userId);
        put("userName", userName);
        put("userEmail",userEmail);
        put("userDepartmentId", userDeptId);
        put("userPhoto",userPhoto);
        put("userPassword",userPassword);
        put("userRoleId",userRoleID);
    }

    public static Users getUser(String username) {
        Users p = null;
        try {
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/Service.svc/Users/%s", baseUrl, username));
            if(a!=null) {
                p = new Users(a.getString("UserId"), a.getString("UserName"), a.getString("UserEmail"), a.getString("UserDepartmentId"), a.getString("UserPhoto"), a.getString("UserPassword"), a.getString("UserRoleId"));
            }

        } catch (Exception e) {
           e.printStackTrace();
        }
        return p;
    }

    public static Users getUserByID(String userID) {
        Users p = null;
        try {
            JSONObject a = JSONParser.getJSONFromUrl(String.format("%s/Service.svc/UsersById/%s", baseUrl, userID));
            if(a!=null) {
                p = new Users(a.getString("UserId"), a.getString("UserName"), a.getString("UserEmail"), a.getString("UserDepartmentId"), a.getString("UserPhoto"), a.getString("UserPassword"), a.getString("UserRoleId"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }


}