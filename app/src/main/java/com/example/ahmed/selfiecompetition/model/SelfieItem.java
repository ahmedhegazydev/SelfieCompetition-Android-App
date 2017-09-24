package com.example.ahmed.selfiecompetition.model;

import java.util.ArrayList;

/**
 * Created by ahmed on 24/09/17.
 */

public class SelfieItem {

    private int numOfLikes = 0;
    private String selfieUrl = "";
    private ArrayList<String> selfieComments = null;
    private String imgUploadedDate = "";
    private String imgName = "";

    //for getting the vale postSnapshot.getValue(SelfieItem.class);

    public String getImgUploadedDate() {
        return imgUploadedDate;
    }

    public void setImgUploadedDate(String imgUploadedDate) {
        this.imgUploadedDate = imgUploadedDate;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public SelfieItem() {
    }

    public SelfieItem(int numOfLikes, String selfieUrl, ArrayList<String> selfieComments, String imgUploadedDate, String imgName) {
        this.numOfLikes = numOfLikes;
        this.selfieUrl = selfieUrl;
        this.selfieComments = selfieComments;
        this.imgUploadedDate = imgUploadedDate;
        this.imgName = imgName;

    }


    public int getNumOfLikes() {
        return numOfLikes;
    }

    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }

    public String getSelfieUrl() {
        return selfieUrl;
    }

    public void setSelfieUrl(String selfieUrl) {
        this.selfieUrl = selfieUrl;
    }

    public ArrayList<String> getSelfieComments() {
        return selfieComments;
    }

    public void setSelfieComments(ArrayList<String> selfieComments) {
        this.selfieComments = selfieComments;
    }
}
