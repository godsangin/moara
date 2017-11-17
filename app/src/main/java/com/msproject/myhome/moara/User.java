package com.msproject.myhome.moara;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanginLee on 2017-11-17.
 */

@IgnoreExtraProperties
public class User {
    public String id;
    public String password;
    public String name;
    public String tel;

    public User() {

    }

    public User(String id, String password, String name, String tel) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.tel = tel;
    }

    @Exclude
    public Map<String, String> toMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("password", password);
        result.put("name", name);
        result.put("tel", tel);

        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
