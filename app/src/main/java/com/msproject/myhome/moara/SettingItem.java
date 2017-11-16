package com.msproject.myhome.moara;

/**
 * Created by SEUNGHYUK SHIN on 2017-11-17.
 */

public class SettingItem {
    int settingIcon;
    String settingName;
    public SettingItem(int settingIcon,String settingName){
        this.settingIcon=settingIcon;
        this.settingName=settingName;
    }

    public int getSettingIcon() {
        return settingIcon;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingIcon(int settingIcon) {
        this.settingIcon = settingIcon;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }
}
