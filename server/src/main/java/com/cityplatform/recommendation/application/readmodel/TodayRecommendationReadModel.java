package com.cityplatform.recommendation.application.readmodel;

import java.util.List;

/**
 * "今日去哪"推荐结果（无合适候选时 cards 为空列表，不返回错误）。
 */
public class TodayRecommendationReadModel {

    private String title;
    private List<RecommendationCardReadModel> cards;

    public TodayRecommendationReadModel(String title, List<RecommendationCardReadModel> cards) {
        this.title = title;
        this.cards = cards;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<RecommendationCardReadModel> getCards() { return cards; }
    public void setCards(List<RecommendationCardReadModel> cards) { this.cards = cards; }
}
