package com.wordpress.zeel.notepad;

/**
 * Created by zeel on 13-07-2016.
 */
public class CardItem {
    private  int icon ;
    private  String title1;
    private  String subtitle;
    private  String time1;
    private String image;
    private int bool;
    private int password;
    private int imagebool;

    public int getIcon() {
        return icon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getBool() {
        return bool;
    }

    public void setBool(int bool) {
        this.bool = bool;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public int getImagebool() {
        return imagebool;
    }

    public void setImagebool(int imagebool) {
        this.imagebool = imagebool;
    }
}
