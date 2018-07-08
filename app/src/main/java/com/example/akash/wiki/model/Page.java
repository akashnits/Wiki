package com.example.akash.wiki.model;

public class Page {

    private String pageId;
    private String title;
    private Thumbnail thumbnail;
    private Terms terms;


    public Page(String pageId, String title, Thumbnail thumbnail, Terms terms) {
        this.pageId = pageId;
        this.title = title;
        this.thumbnail = thumbnail;
        this.terms = terms;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Terms getTerms() {
        return terms;
    }

    public void setTerms(Terms terms) {
        this.terms = terms;
    }

    @Override
    public String toString() {
        return "PagesItem{" +
                ",terms = '" + terms + '\'' +
                ",pageid = '" + pageId + '\'' +
                ",title = '" + title + '\'' +
                ",thumbnail = '" + thumbnail + '\'' +
                "}";
    }
}
