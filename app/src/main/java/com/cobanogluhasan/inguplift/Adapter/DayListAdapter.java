package com.cobanogluhasan.inguplift.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cobanogluhasan.inguplift.DaysActivity;
import com.cobanogluhasan.inguplift.R;
import com.cobanogluhasan.inguplift.SynonymActivity;

import java.util.ArrayList;

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.ViewHolder> {

    private static final String TAG = "DayListAdapter";
    private ArrayList<String> mDays = new ArrayList<>();
    private Context mContext;

    public DayListAdapter(ArrayList<String> mDays, Context mContext) {
        this.mDays = mDays;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.days_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.days.setText(mDays.get(position));
        holder.days.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));

        holder.parentLayout.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: pressed");

                Intent synonymIntent = new Intent(mContext, SynonymActivity.class);
                synonymIntent.putExtra("Days", mDays.get(position));
                mContext.startActivity(synonymIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView days;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            days = (TextView) itemView.findViewById(R.id.days);
            parentLayout = (RelativeLayout) itemView.findViewById(R.id.parentLayout);

        }
    }


}
