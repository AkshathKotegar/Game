package com.improstech.housie.app.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.improstech.housie.app.R;
import com.improstech.housie.app.activity.GameActivity;

import java.util.ArrayList;

/**
 * Created by User2 on 13-07-2017.
 */

public class GridAdapter extends ArrayAdapter<String> {
    ArrayList<String> data;
    ArrayList<String> drawnArrList;
    ArrayList<String> nosToRecycler;
    private Context context;
    private int layoutResourceId;

    ViewHolder holder = null;

    public GridAdapter(Context context, int resource, ArrayList<String> data, ArrayList<String> drawnArrList, ArrayList<String> nosToRecycler) {
        super(context, resource);
        this.context = context;
        this.data = data;
        this.layoutResourceId = resource;
        this.drawnArrList = drawnArrList;
        this.nosToRecycler = nosToRecycler;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;


        if (row == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.topText = (TextView) row.findViewById(R.id.txtNo);
            holder.cardNos = (CardView) row.findViewById(R.id.cardNos);
            holder.frameChecked = (ImageView) row.findViewById(R.id.frameChecked);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final String cardHolderName = data.get(position);
        holder.topText.setText(cardHolderName);
        //boolean found = containsDrawnNumbers(drawnArrList,cardHolderName);
        if (containsDrawnNumbers(drawnArrList, cardHolderName))
            if (containsDrawnNumbers(nosToRecycler, cardHolderName))
                if (!cardHolderName.equals(" "))
                    holder.cardNos.setBackgroundColor(Color.parseColor("#44000000"));
        holder.topText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GameActivity.clicked=true;
                //holder.frameChecked.setVisibility(View.VISIBLE);
                if (containsDrawnNumbers(nosToRecycler, cardHolderName))
                GameActivity.selectedNosArr.add(cardHolderName);
                else
                    Toast.makeText(context, "You cannot select a number which is not drawn", Toast.LENGTH_LONG).show();
                //holder.cardNos.setBackgroundColor(Color.parseColor("#44000000"));
                //Toast.makeText(context, cardHolderName + " @ pos " + position, Toast.LENGTH_LONG).show();
            }
        });
        return row;
    }

    private static class ViewHolder {
        TextView topText;
        CardView cardNos;
        ImageView frameChecked;
    }

    boolean containsDrawnNumbers(ArrayList<String> drawnArrList, String pickName) {
        for (String p : drawnArrList) {
            if (p.equals(pickName)) {
                return true;
            }
        }
        return false;
    }
}
