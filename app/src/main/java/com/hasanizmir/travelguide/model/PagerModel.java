package com.hasanizmir.travelguide.model;

public class PagerModel {
    private int resId;
    private String placeName, placeDetail;

    public PagerModel(int resId, String placeName) {
        this.resId = resId;
        this.placeName = placeName;
    }

    public PagerModel(int resId, String placeName, String placeDetail) {
        this.resId = resId;
        this.placeName = placeName;
        this.placeDetail = placeDetail;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceDetail() {
        return placeDetail;
    }

    public void setPlaceDetail(String placeDetail) {
        this.placeDetail = placeDetail;
    }
}
