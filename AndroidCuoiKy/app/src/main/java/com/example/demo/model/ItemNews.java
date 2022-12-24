package com.example.demo.model;

public class ItemNews {
    private String name;
    private int resourceImage;
    private String detailInfo;

    public ItemNews(String name, int resourceImage, String detailInfo) {
        this.name = name;
        this.resourceImage = resourceImage;
        this.detailInfo = detailInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public int getResourceImage() {
        return resourceImage;
    }

    public void setResourceImage(int resourceImage) {
        this.resourceImage = resourceImage;
    }
}
