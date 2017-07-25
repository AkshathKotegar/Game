package com.improstech.housie.app.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.improstech.housie.app.R;

import java.util.ArrayList;

/**
 * Created by User2 on 14-07-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<String> dataSet;
    int res;
    int color;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;

        //  ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.txtView);
            //this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            //  this.imageViewIcon = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getPosition() == 0) {
                        Toast.makeText(v.getContext(), " On CLick one", Toast.LENGTH_SHORT).show();

                    }
                    if (getPosition() == 1) {
                        Toast.makeText(v.getContext(), " On CLick Two", Toast.LENGTH_SHORT).show();

                    }
                    if (getPosition() == 2) {
                        Toast.makeText(v.getContext(), " On CLick Three", Toast.LENGTH_SHORT).show();

                    }
                    if (getPosition() == 3) {
                        Toast.makeText(v.getContext(), " On CLick Fore", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }

    public CustomAdapter(ArrayList<String> data, int res, int color) {
        this.dataSet = data;
        this.res = res;
        this.color = color;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(res, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        // TextView textViewVersion = holder.textViewVersion;
        //  ImageView imageView = holder.imageViewIcon;
        textViewName.setTextColor(color);
        textViewName.setText(dataSet.get(listPosition));
        //textViewVersion.setText(dataSet.get(listPosition).getVersion());
        // imageView.setImageResource(dataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}