package com.haroldfritsch.rssfeedaggregator.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fritsc_h on 14/01/2017.
 */

public class Source {
    private String categoryId;
    private String id;
    private String name;
    private String url;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
