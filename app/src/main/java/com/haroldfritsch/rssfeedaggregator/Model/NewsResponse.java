package com.haroldfritsch.rssfeedaggregator.Model;

import java.util.List;

/**
 * Created by fritsc_h on 15/01/2017.
 */

public class NewsResponse {
    private Category category;
    private List<News> news;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
