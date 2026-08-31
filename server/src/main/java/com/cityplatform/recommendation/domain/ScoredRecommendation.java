package com.cityplatform.recommendation.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 评分后的推荐结果（Domain 输出对象）。
 * score 由 RecommendationDomainService 的规则累加，reason 由命中规则的解释片段拼接而成。
 */
public class ScoredRecommendation {

    private final RecommendationCandidate candidate;
    private int score;
    private final List<String> reasonParts = new ArrayList<>();

    public ScoredRecommendation(RecommendationCandidate candidate) {
        this.candidate = candidate;
        this.score = 0;
    }

    /**
     * 累加一项规则得分并记录可解释的理由片段。
     */
    public void addPoints(int points, String reasonPart) {
        this.score += points;
        reasonParts.add(reasonPart);
    }

    public RecommendationCandidate getCandidate() { return candidate; }
    public int getScore() { return score; }
    public String getReason() { return String.join("，", reasonParts); }
}
