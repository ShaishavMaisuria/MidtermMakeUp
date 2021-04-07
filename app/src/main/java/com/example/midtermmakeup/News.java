package com.example.midtermmakeup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class News {

    String title,author,description,publishedAt;
    String urlToImage,url;

    public News(JSONObject body) throws JSONException  {
        title=body.getString("title");
        author=body.getString("author");

        description=body.getString("description");
        publishedAt=body.getString("publishedAt");
        urlToImage=body.getString("urlToImage");
        url=body.getString("url");
//        author=body.getJSONArray("author").getJSONObject(0).getString("name").toString();
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
