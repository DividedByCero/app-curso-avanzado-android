package com.example.example.appfinalavanzadoandroid.models;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

public class Post {
    public String title;
    public String fileName;
    public String author;
    public Bitmap image;
    private Uri downloadUrl;
    public Date date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Uri getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(Uri downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public Bitmap getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
