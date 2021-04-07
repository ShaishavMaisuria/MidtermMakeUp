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
    public void NewsSourceToSelectedNews(Source selectedSource) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, SelectedNews.newInstance(selectedSource) , "SourceCategories")
                .addToBackStack(null)
                .commit();
    }
}