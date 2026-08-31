package com.cityplatform.recommendation.application;

import com.cityplatform.recommendation.application.readmodel.ActivityCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.RouteCandidateReadModel;
import com.cityplatform.recommendation.application.readmodel.StreetAreaLocationReadModel;
import com.cityplatform.recommendation.application.readmodel.StoreCandidateReadModel;

import java.util.List;

/**
 * 推荐候选查询接口（Application 层定义，Infrastructure 实现）。
 * 跨域候选聚合通过 SQL JOIN 完成（读模型投影，遵循现有 QueryRepository 模式），
 * 并在此处完成硬过滤：门店营业中、活动已发布且未结束、内容未停用。
 */
public interface RecommendationQueryRepository {

    /**
     * 营业中门店候选（status=ACTIVE 且 business_status=OPEN）。
     * streetAreaId 非空时仅保留在该街区存在 ACTIVE STORE 类型 POI 的门店。
     * hasCoupon 表示门店当前存在生效期内优惠券。
     */
    List<StoreCandidateReadModel> findStoreCandidates(Long streetAreaId);

    /**
     * 进行中/未开始活动候选（status=PUBLISHED 且 end_time 未过）。
     */
    List<ActivityCandidateReadModel> findActivityCandidates(Long streetAreaId);

    /**
     * 启用中的体验路线候选（status=ACTIVE）。
     */
    List<RouteCandidateReadModel> findRouteCandidates(Long streetAreaId);

    /**
     * 街区中心坐标（推荐距离锚点；街区不存在或已停用时返回 null）。
     */
    StreetAreaLocationReadModel findStreetAreaCenter(Long streetAreaId);
}
