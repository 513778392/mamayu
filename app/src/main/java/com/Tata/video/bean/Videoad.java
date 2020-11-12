package com.Tata.video.bean;

public class Videoad {
    String des;
    String name;
    String thumb;
    String time;

    @Override
    public String toString() {
        return "Videoad{" +
                "des='" + des + '\'' +
                ", name='" + name + '\'' +
                ", thumb='" + thumb + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
