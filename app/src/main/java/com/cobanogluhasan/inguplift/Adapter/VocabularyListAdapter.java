package com.cobanogluhasan.inguplift.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.cobanogluhasan.inguplift.R;
import com.cobanogluhasan.inguplift.Word;

import java.util.ArrayList;

public class VocabularyListAdapter extends RecyclerView.Adapter<VocabularyListAdapter.ViewHolder> {

        private static final String TAG = "DayListAdapter";
        private ArrayList<Word> wordList = new ArrayList<>();
        private Context mContext;



        public VocabularyListAdapter(Context mContext,ArrayList<Word> wordList) {
            this.wordList = wordList;
            this.mContext = mContext;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vocabulary_item_2, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;

        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            Log.d(TAG, "onBindViewHolder: called");


            holder.myLayout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

            final Word mWord = wordList.get(position);

            holder.itemWord.setText(mWord.getWord());
            holder.itemSynonym.setText(mWord.getSynonym());


            holder.myLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(mContext)
                            .setTitle(mWord.getWord())
                            .setMessage(mWord.getSynonym()+ "\n\n"+ mWord.getDefinition() + "\n\n" + mWord.getExample()).show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return wordList.size();
        }


    public class ViewHolder extends RecyclerView.ViewHolder{

            TextView itemWord;
            TextView itemSynonym;
          RelativeLayout myLayout;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemWord = (TextView) itemView.findViewById(R.id.itemWord);
                itemSynonym = (TextView) itemView.findViewById(R.id.itemSynonym);
                myLayout = (RelativeLayout) itemView.findViewById(R.id.myLayout);
            }
        }


    }


