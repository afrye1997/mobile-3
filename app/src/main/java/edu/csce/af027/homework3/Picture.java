package edu.csce.af027.homework3;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.net.URI;

@Entity (tableName = "picture_database")
public class Picture implements Serializable {
    @PrimaryKey
    @NonNull
    public String title;

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;

    @ColumnInfo(name = "picturePath")
    public String picturePath;

    public Picture() {}

    public Picture(String title, Double latitude, Double longitude, String picturePath) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.picturePath = picturePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "title='" + title + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", picturePath='" + picturePath + '\'' +
                '}';
    }
}

