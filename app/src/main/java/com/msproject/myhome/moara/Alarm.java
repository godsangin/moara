package com.msproject.myhome.moara;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Alarm {
    boolean gift;
    boolean buy;
    boolean save;

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("gift", gift);
        result.put("buy", buy);
        result.put("save", save);
        return result;
    }

    public Alarm() {
    }

    public boolean isGift() {
        return gift;
    }

    public void setGift(boolean gift) {
        this.gift = gift;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public Alarm(boolean gift, boolean buy, boolean save){
        this.gift = gift;
        this.buy = buy;
        this.save = save;
    }

}
