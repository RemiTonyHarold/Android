package com.haroldfritsch.rssfeedaggregator;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by fritsc_h on 15/01/2017.
 */

public class RSSFeedAggregator extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("GT-Walsheim-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
