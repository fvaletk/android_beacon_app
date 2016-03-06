package com.example.filibertovaletk.exitoone.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filibertovaletk.exitoone.R;
import com.example.filibertovaletk.exitoone.models.Promotions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by filibertovaletk on 3/6/16.
 */
public class PromotionAdapter extends ArrayAdapter<Promotions>{

    private Context context;
    private ArrayList<Promotions> promotionsList;

    public PromotionAdapter(Context context, int resource, ArrayList<Promotions> objects) {
        super(context, resource, objects);
        this.context = context;
        this.promotionsList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_row, null);

        Promotions promotion = promotionsList.get(position);

        ImageView img = (ImageView) view.findViewById(R.id.thumbnail);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView description = (TextView) view.findViewById(R.id.description);
        TextView date = (TextView) view.findViewById(R.id.date);

        Picasso.with(getContext()).load(promotion.getImage_url()).into(img);
        title.setText(promotion.getTitle());
        description.setText(promotion.getDescription());
        date.setText(promotion.getDate());

        return view;
    }
}
