package com.example.midtermmakeup;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class NewsSource extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_Selected_Category = "ARG_PARAM_Selected_Category";
    private static final String TAG = "NewsSource";


    // TODO: Rename and change types of parameters
    private NewsData.Category mCategory;


    public NewsSource() {
        // Required empty public constructor
    }

    public static NewsSource newInstance(NewsData.Category param1) {
        NewsSource fragment = new NewsSource();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_Selected_Category, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = (NewsData.Category) getArguments().getSerializable(ARG_PARAM_Selected_Category);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_news_source, container, false);
        getActivity().setTitle(mCategory.name);
        getSources();


        recyclerView = view.findViewById(R.id.newsSourceRecylerView);

        adapter = new NewsSourcesAdapter(sourceArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return view;
    }
    private final OkHttpClient client = new OkHttpClient();

    RecyclerView recyclerView;
    NewsSourcesAdapter adapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<Source> sourceArrayList=new ArrayList<>();

    void getSources(){

        HttpUrl url = HttpUrl.parse("https://newsapi.org/v2/sources").newBuilder()
                .addQueryParameter("apiKey","e44f8670efb643338438236c964fda65")
                .addQueryParameter("category",mCategory.category)
                .addQueryParameter("language","en")
                .addQueryParameter("country","us")
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
                    sourceArrayList.clear();
                    String body = responseBody.string();
                    Log.d(TAG,"OnResponse Succesfull " + body);
                    try {
                        JSONObject rootObject = new JSONObject(body);
                        JSONArray sourcelist= rootObject.getJSONArray("sources");
                        for(int i=0;i<sourcelist.length();i++){
                            Source source = new Source(sourcelist.getJSONObject(i));
                            sourceArrayList.add(source);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG,  "source Arraylist"+sourceArrayList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                }else{
                    String body = responseBody.string();
                    Log.d(TAG,"OnResponse unSuccesfull " + body);
                }
            }
        });
    }

class NewsSourcesAdapter extends RecyclerView.Adapter<NewsSourcesAdapter.NewsSourcesViewHolder>{

        ArrayList<Source> sourcelist;
    public NewsSourcesAdapter(ArrayList arrayList) {
        this.sourcelist=arrayList;
    }

    @NonNull
    @Override
    public NewsSourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_each_source, parent, false);
        NewsSourcesViewHolder userViewHolder = new NewsSourcesViewHolder(view);

        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsSourcesViewHolder holder, int position) {
        Source source =sourcelist.get(position);

        holder.setupNewSources(source);

    }

    @Override
    public int getItemCount() {
        return sourcelist.size();
    }

    public class NewsSourcesViewHolder extends RecyclerView.ViewHolder{
            TextView sourceName,sourceDescription;
            Source eachSource;
            public NewsSourcesViewHolder(@NonNull View itemView) {
                super(itemView);
                sourceName=itemView.findViewById(R.id.textViewSourceName);
                sourceDescription=itemView.findViewById(R.id.textViewSourceDescription);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.NewsSourceToSelectedNews(eachSource);
                    }
                });
            }

        public void setupNewSources(Source source){
                Log.d(TAG,"source each"+source.toString());
            this.eachSource=source;
            sourceName.setText(eachSource.name);
            sourceDescription.setText(eachSource.description);
        }
        }

}


    NewsSourceListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (NewsSourceListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NewsSourceListener");
        }
    }

    interface NewsSourceListener {
        void NewsSourceToSelectedNews(Source selectedSource);
    }

}