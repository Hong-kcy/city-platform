package com.cityplatform.recommendation.infrastructure.mapper;

import com.cityplatform.recommendation.application.readmodel.ActivityCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.RouteCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.StreetAreaLocationReadModel;
import com.cityplatform.recommendation.application.readmodel.StoreCandidateReadModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 推荐候选查询 Mapper（跨域 JOIN 组装候选投影，遵循 QueryRepository 读模型模式）。
 */
@Mapper
public interface RecommendationQueryMapper {

    List<StoreCandidateReadModel> selectStoreCandidates(@Param("streetAreaId") Long streetAreaId);

    List<ActivityCandidateReadModel> selectActivityCandidates(@Param("streetAreaId") Long streetAreaId);

    List<RouteCandidateReadModel> selectRouteCandidates(@Param("streetAreaId") Long streetAreaId);

    StreetAreaLocationReadModel selectStreetAreaCenter(@Param("streetAreaId") Long streetAreaId);
}
