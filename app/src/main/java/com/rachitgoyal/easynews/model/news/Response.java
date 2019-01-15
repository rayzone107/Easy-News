package com.rachitgoyal.easynews.model.news;

import java.util.List;

/**
 * Created by Rachit Goyal on 11/01/19.
 */
public class Response {
    private String status;
    private int totalResults;
    private List<Article> articles;

    public Response(String status, int totalResults, List<Article> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
