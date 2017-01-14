package com.haroldfritsch.rssfeedaggregator.Adapter;

import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by fritsc_h on 14/01/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_news, parent, false);
        }
        NewsHolder holder = (NewsHolder) convertView.getTag();
        if (holder == null) {
            holder = new NewsHolder();
            holder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
            holder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
            holder.tvDescription = (TextView)convertView.findViewById(R.id.tvDescription);
            convertView.setTag(holder);
        }

        News news = getItem(position);
        if (news != null) {
            holder.tvTitle.setText(news.getTitle());
            holder.tvDescription.setText(news.getDescription());
            long now = System.currentTimeMillis();
            holder.tvDate.setText(DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(news.getDateCreation()),
                    now,
                    DateUtils.FORMAT_ABBREV_RELATIVE));
        }

        return convertView;
    }



    private class NewsHolder {
        TextView tvTitle;
        TextView tvDate;
        TextView tvDescription;
    }
}
