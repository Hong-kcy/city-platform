package com.cityplatform.recommendation.infrastructure;

import com.cityplatform.recommendation.application.RecommendationQueryRepository;
import com.cityplatform.recommendation.application.readmodel.ActivityCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.RouteCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.StreetAreaLocationReadModel;
import com.cityplatform.recommendation.application.readmodel.StoreCandidateReadModel;
import com.cityplatform.recommendation.infrastructure.mapper.RecommendationQueryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 推荐候选查询实现。仅读模型投影，不返回任何业务域 Entity。
 */
@Repository
public class RecommendationQueryRepositoryImpl implements RecommendationQueryRepository {

    private final RecommendationQueryMapper mapper;

    public RecommendationQueryRepositoryImpl(RecommendationQueryMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<StoreCandidateReadModel> findStoreCandidates(Long streetAreaId) {
        return mapper.selectStoreCandidates(streetAreaId);
    }

    @Override
    public List<ActivityCandidateReadModel> findActivityCandidates(Long streetAreaId) {
        return mapper.selectActivityCandidates(streetAreaId);
    }

    @Override
    public List<RouteCandidateReadModel> findRouteCandidates(Long streetAreaId) {
        return mapper.selectRouteCandidates(streetAreaId);
    }

    @Override
    public StreetAreaLocationReadModel findStreetAreaCenter(Long streetAreaId) {
        return mapper.selectStreetAreaCenter(streetAreaId);
    }
}
