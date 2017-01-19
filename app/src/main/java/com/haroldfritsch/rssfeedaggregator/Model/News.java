package com.haroldfritsch.rssfeedaggregator.Model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by fritsc_h on 14/01/2017.
 */

public class News extends RealmObject {
    private String creator;
    private String dateCreation;
    private String description;
    private String guid;
    @PrimaryKey
    private String id;
    private String categoryId;
    private String link;
    @SerializedName("tata")
    private String pubDate;
    @SerializedName("pubDate")
    private Date parsedPubDate;
    private String sourceId;
    private String thumbnail;
    private String title;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

/*    public void setPubDate(String pubDate) {
        this.pubDate = getDateFormatted(pubDate);
    }*/

    public Date getParsedPubDate() {
        if (parsedPubDate != null) {
            return parsedPubDate;
        } else if (pubDate != null) {
            return getDateFormatted(pubDate);
        } else {
            return new Date();
        }
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
        this.parsedPubDate = getDateFormatted(pubDate);
    }

    private Date getDateFormatted(String rawDate) {
        //Sat, 14 Jan 2017 13:19:53 GMT
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        try {
            return simpleDateFormat.parse(rawDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}