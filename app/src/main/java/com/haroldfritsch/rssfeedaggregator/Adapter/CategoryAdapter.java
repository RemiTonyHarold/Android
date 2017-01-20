package com.haroldfritsch.rssfeedaggregator.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.haroldfritsch.rssfeedaggregator.Model.Category;
import com.haroldfritsch.rssfeedaggregator.Model.News;
import com.haroldfritsch.rssfeedaggregator.R;

/**
 * Created by fritsc_h on 20/01/2017.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    @LayoutRes
    int resource;

    public CategoryAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        CategoryHolder holder = (CategoryHolder) convertView.getTag();
        if (holder == null) {
            holder = new CategoryHolder();
            holder.tvCat = (TextView)convertView.findViewById(R.id.tvCat);
            convertView.setTag(holder);
        }

        Category category = getItem(position);
        if (category != null) {
            holder.tvCat.setText(category.getName());
        }

        return convertView;
    }

    private class CategoryHolder {
        TextView tvCat;
    }
}