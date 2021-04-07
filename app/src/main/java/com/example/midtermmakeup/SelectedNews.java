package com.example.midtermmakeup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private static final String ARG_PARAM_Selected_Source_ID = "ARG_PARAM_Selected_Source_ID";
    private static final String TAG ="SelectedNews";


    // TODO: Rename and change types of parameters
    private String mSourceName;
    private String mid;

    public SelectedNews() {
        // Required empty public constructor
    }


    public static SelectedNews newInstance(String param1,String param2) {
        SelectedNews fragment = new SelectedNews();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_Selected_Source, param1);
        args.putString(ARG_PARAM_Selected_Source_ID,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSourceName =  getArguments().getString(ARG_PARAM_Selected_Source);
            mid= getArguments().getString(ARG_PARAM_Selected_Source_ID);
            Log.d(TAG,"id "+mid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_selected_news, container, false);
        getActivity().setTitle(mSourceName);

        getSelectedNews();

        recyclerView = view.findViewById(R.id.selectedNewsRecylerView);
        adapter = new SelectedNewsAdapter(newsArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        return view;
    }


    private final OkHttpClient client = new OkHttpClient();

    RecyclerView recyclerView;
    SelectedNewsAdapter adapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<News> newsArrayList=new ArrayList<>();

    void getSelectedNews(){


        Log.d(TAG,"id "+mid);
        HttpUrl url = HttpUrl.parse("https://newsapi.org/v2/everything").newBuilder()
                .addQueryParameter("apiKey","e44f8670efb643338438236c964fda65")
                .addQueryParameter("sources",mid)
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

    class SelectedNewsAdapter extends RecyclerView.Adapter<SelectedNewsAdapter.SelectedNewsViewHolder>{
        ArrayList<News> newsList;
        public SelectedNewsAdapter(ArrayList arrayList) {
            this.newsList=arrayList;
        }

        @NonNull
        @Override
        public SelectedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_each_news, parent, false);
            SelectedNewsViewHolder userViewHolder = new SelectedNewsViewHolder(view);

            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SelectedNewsViewHolder holder, int position) {
            News news=newsList.get(position);
            holder.setupSelectedNews(news);
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        public class SelectedNewsViewHolder extends RecyclerView.ViewHolder{
            TextView title,authorName,createAt,description;
            ImageView sourceImage;
            News mNews;
            public SelectedNewsViewHolder(@NonNull View itemView) {
                super(itemView);

                title=itemView.findViewById(R.id.textViewNewsTitleSelectedEachNews);
                authorName=itemView.findViewById(R.id.textViewAuthorNameSelectedEachNews);
                createAt=itemView.findViewById(R.id.textViewNewsDateSelectedEachNews);
                description=itemView.findViewById(R.id.textViewNewsDesciptionSelectedEachNews);
                sourceImage=itemView.findViewById(R.id.imageView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mNews.url));
                        startActivity(browserIntent);
                    }
                });
            }

            public void setupSelectedNews(News news){
                this.mNews=news;
                title.setText(mNews.title);
                authorName.setText(mNews.author);
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    Date date1=input.parse(mNews.publishedAt);
                    Log.d(TAG,"DATE "+date1);
                    String finalDate=formatter.format(date1);
                    Log.d(TAG,"Final Date "+finalDate   );
                    createAt.setText(finalDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

//                Log(TAG,"date"+formatter.format(mNews.publishedAt ));


                description.setText(mNews.description);
                Picasso.get().load(mNews.urlToImage).into(sourceImage);

            }
        }
    }





}