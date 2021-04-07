package com.example.midtermmakeup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectedNews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectedNews extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_Selected_Source = "ARG_PARAM_Selected_Source";
    private static final String TAG ="SelectedNews";


    // TODO: Rename and change types of parameters
    private String mSourceName;


    public SelectedNews() {
        // Required empty public constructor
    }


    public static SelectedNews newInstance(Source param1) {
        SelectedNews fragment = new SelectedNews();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_Selected_Source, param1.name);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSourceName =  getArguments().getString(ARG_PARAM_Selected_Source);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_selected_news, container, false);
        getActivity().setTitle(mSourceName);

        getSelectedNews();
        return view;
    }


    private final OkHttpClient client = new OkHttpClient();

    RecyclerView recyclerView;
    NewsSource.NewsSourcesAdapter adapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<News> newsArrayList=new ArrayList<>();

    void getSelectedNews(){

        HttpUrl url = HttpUrl.parse("https://newsapi.org/v2/everything").newBuilder()
                .addQueryParameter("apiKey","e44f8670efb643338438236c964fda65")
                .addQueryParameter("sources",mSourceName)
                .addQueryParameter("language","en")
                .build();

        Log.d(TAG,"get sources https"+url);


        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if(response.isSuccessful()){

                    newsArrayList.clear();
                    String body = responseBody.string();
                    Log.d(TAG,"OnResponse Succesfull " + body);
                    try {
                        JSONObject rootObject = new JSONObject(body);
                        JSONArray sourcelist= rootObject.getJSONArray("articles");
                        for(int i=0;i<sourcelist.length();i++){
                            News newsarticle = new News(sourcelist.getJSONObject(i));
                            newsArrayList.add(newsarticle);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG,  "News Arraylist"+newsArrayList);
                    //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            adapter.notifyDataSetChanged();
//                        }
//                    });

                }else{
                    String body = responseBody.string();
                    Log.d(TAG,"OnResponse unSuccesfull " + body);
                }
            }
        });
    }




}