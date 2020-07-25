package com.cobanogluhasan.inguplift.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cobanogluhasan.inguplift.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

public class SynonymAdapter extends RecyclerView.Adapter<SynonymAdapter.ViewHolder> {

    private static final String TAG = "SynonymAdapter";

    private ArrayList<String> synonymList = new ArrayList<>();
    private ArrayList<String> synonymExampleList = new ArrayList<>();
    private Context mContext;



    public SynonymAdapter(Context context, ArrayList<String> synonym, ArrayList<String> synonymExample) {

        this.synonymList = synonym;
        this.synonymExampleList =synonymExample ;
        this.mContext = context;
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("oncreateviewholder","called" );

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.card_item_synonym, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("onbindviewholder","called" );
        holder.word.setText(synonymList.get(position));

        holder.definition.setText(synonymExampleList.get(position));


    }

    @Override
    public int getItemCount() {
        return synonymList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView word;
        TextView definition;
        DiscreteScrollView scrollView;



        public ViewHolder(View itemView) {
            super(itemView);

            word = itemView.findViewById(R.id.word);
            definition = itemView.findViewById(R.id.definition);
            scrollView = itemView.findViewById(R.id.scroll);

        }





    }




}
