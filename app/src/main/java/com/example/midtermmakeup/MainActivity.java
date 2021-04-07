package com.example.midtermmakeup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements NewCategories.NewCategoriesListener, NewsSource.NewsSourceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new NewCategories(), "NewCategories")
                .commit();

    }


    @Override
    public void categoriesToSourcesfragment(NewsData.Category selectedCategory) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, NewsSource.newInstance(selectedCategory), "SourceCategories")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void NewsSourceToSelectedNews(String selectedSourcename, String id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, SelectedNews.newInstance(selectedSourcename,id) , "SourceCategories")
                .addToBackStack(null)
                .commit();
    }
}