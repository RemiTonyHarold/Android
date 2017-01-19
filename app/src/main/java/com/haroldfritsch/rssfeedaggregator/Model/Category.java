package com.haroldfritsch.rssfeedaggregator.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by fritsc_h on 14/01/2017.
 */

public class Category extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
