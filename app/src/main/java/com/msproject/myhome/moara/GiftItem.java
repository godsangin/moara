package com.msproject.myhome.moara;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class GiftItem {
    public String date;
    public String from;
    public String name;
    public String storeUid;

    public GiftItem() {

    }

    public GiftItem(String name, String date, String from, String storeUid){
        this.name = name;
        this.date=date;
        this.from=from;
        this.storeUid= storeUid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("date", date);
        result.put("from", from);
        result.put("storeUid", storeUid);

        return result;
    }

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getStoreUid(){return storeUid;}
    public void setStoreUid(String storeUid){this.storeUid = storeUid;}

    public String getDate(){return date;}
    public void setDate(String date){this.date=date;}

    public String getFrom(){return from;}
    public void setFrom(String from){this.from=from;}

}