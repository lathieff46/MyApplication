package com.artqueen.logicuniversitystationerysystem;

import java.util.HashMap;

/**
 * Created by shaikmdashiq on 1/3/15.
 */
public class CartItems extends HashMap<String, String> {


    public CartItems(String itemId, String category, String description, String quantity,String unitOfMeasure) {
        put("itemId", itemId);
        put("category", category);
        put("description", description);
        put("qty",quantity);
        put("unitOfMeasure", unitOfMeasure);
    }


}

