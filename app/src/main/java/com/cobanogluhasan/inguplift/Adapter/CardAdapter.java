package com.cobanogluhasan.inguplift.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cobanogluhasan.inguplift.DatabaseHelper;
import com.cobanogluhasan.inguplift.Model.Model;
import com.cobanogluhasan.inguplift.R;
import com.huxq17.swipecardsview.BaseCardAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends BaseCardAdapter {

    private List<Model> modelList;
    private Context context;
    private DatabaseHelper databaseHelper;

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

        databaseHelper = new DatabaseHelper(context);

        ImageView imageView = (ImageView) cardview.findViewById(R.id.imageview);
        ImageButton addToListButton = (ImageButton) cardview.findViewById(R.id.addToListButton);

        TextView textView = (TextView) cardview.findViewById(R.id.textview);
        TextView word = (TextView)  cardview.findViewById(R.id.word);

        final Model model = modelList.get(position);
        word.setText(model.getTitle());

        textView.setText(model.getWord());
        Picasso.with(context).load(model.getImage()).into(imageView);

        addToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.addData(model.getTitle(), "",model.getWord(),"");
            }
        });



    }


}
