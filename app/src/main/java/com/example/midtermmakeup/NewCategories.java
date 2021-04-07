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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCategories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCategories extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "NewCategories" ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewCategories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewCategories.
     */
    // TODO: Rename and change types and number of parameters
    public static NewCategories newInstance(String param1, String param2) {
        NewCategories fragment = new NewCategories();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    UsersAdapter adapter;
    ArrayList<NewsData.Category> categoryArrayList;
    RecyclerView recyclerView;
    NewsCategoriesAdapter adapter;
    LinearLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_new_categories, container, false);
        getActivity().setTitle("News Source Categories");

        categoryArrayList=NewsData.categories;
        Log.d(TAG,"arrayList"+categoryArrayList.toString());


        recyclerView = view.findViewById(R.id.newsCategoriesRecylerView);
        adapter = new NewsCategoriesAdapter(categoryArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return view;
    }


class NewsCategoriesAdapter extends RecyclerView.Adapter<NewsCategoriesAdapter.NewCategoriesViewHolder>{
ArrayList<NewsData.Category> categoryList;
    public NewsCategoriesAdapter(ArrayList arrayList) {
        this.categoryList=arrayList;
    }

    @NonNull
    @Override
    public NewCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_each_categories, parent, false);
        NewCategoriesViewHolder userViewHolder = new NewCategoriesViewHolder(view);

        return userViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull NewCategoriesViewHolder holder, int position) {
        NewsData.Category category= categoryList.get(position);
        holder.setupNewCategories(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class NewCategoriesViewHolder extends RecyclerView.ViewHolder{

        TextView textViewcategory;
        NewsData.Category newCategory;
            public NewCategoriesViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewcategory =itemView.findViewById(R.id.textViewEachCategories);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"adapter listener"+newCategory);
                        mListener.categoriesToSourcesfragment(newCategory);
                    }
                });
            }
            public void setupNewCategories(NewsData.Category category){
                this.newCategory=category;
                textViewcategory.setText(category.name);
            }

        }
}
    NewCategoriesListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (NewCategoriesListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NewCategoriesListener");
        }
    }

    interface NewCategoriesListener {
        void categoriesToSourcesfragment(NewsData.Category selectedCategory);
}
}