package com.cobanogluhasan.inguplift.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cobanogluhasan.inguplift.Model.Model;
import com.cobanogluhasan.inguplift.R;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends BaseCardAdapter {

    private List<Model> modelList;
    private Context context;

    public CardAdapter(List<Model> modelList, Context context) {

        this.modelList = modelList;
        this.context = context;

    }


    @Override
    public int getCount() {

        return modelList.size();

    }

    @Override
    public int getCardLayoutId() {

        return R.layout.card_item;

    }

    @Override
    public void onBindData(int position, View cardview) {

        if(modelList == null || modelList.size()==0 ) {

            return;
        }

        ImageView imageView = (ImageView) cardview.findViewById(R.id.imageview);

        TextView textView = (TextView) cardview.findViewById(R.id.textview);
        TextView word = (TextView)  cardview.findViewById(R.id.word);

        Model model = modelList.get(position);
        word.setText(model.getTitle());

        textView.setText(model.getWord());
        Picasso.with(context).load(model.getImage()).into(imageView);



    }


}
