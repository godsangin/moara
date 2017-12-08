package com.msproject.myhome.moara;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GiftItem {
    public String date;
    public String from;
    public String name;
    public int resId;

    public GiftItem() {

    }

    public GiftItem(String name, String date, String from, int resId){
        this.name = name;
        this.date=date;
        this.from=from;
        this.resId= resId;
    }

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public int getResId(){return resId;}
    public void setResId(int resId){this.resId=resId;}

    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}

    public String getFrom(){return from;}
    public void setFrom(String from){this.from=from;}

}