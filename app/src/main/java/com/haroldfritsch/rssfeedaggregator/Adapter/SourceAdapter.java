package com.haroldfritsch.rssfeedaggregator.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.haroldfritsch.rssfeedaggregator.Model.Category;
import com.haroldfritsch.rssfeedaggregator.Model.Source;
import com.haroldfritsch.rssfeedaggregator.R;

/**
 * Created by fritsc_h on 20/01/2017.
 */

public class SourceAdapter extends ArrayAdapter<Source> {
    @LayoutRes
    int resource;

    public SourceAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        SourceHolder holder = (SourceHolder) convertView.getTag();
        if (holder == null) {
            holder = new SourceHolder();
            holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        }

        Source source = getItem(position);
        if (source != null) {
            holder.tvName.setText(source.getName());
        }

        return convertView;
    }

    private class SourceHolder {
        TextView tvName;
    }
}