package com.example.midtermmakeup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Source {
String description, name,id;

    public Source(JSONObject body) throws JSONException {

        name = body.getString("name");
        description=body.getString("description");
        id=body.getString("id");
    }

    @Override
    public String toString() {
        return "Source{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
//    @Override
//    public String toString() {
//        return "Source{" +
//                "description='" + description + '\'' +
//                ", name='" + name + '\'' +
//                '}';
//    }
}
