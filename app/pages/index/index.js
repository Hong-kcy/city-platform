const { request, resolveImageUrl, toastError } = require('../../utils/request');

Page({
  data: {
    streetAreas: [],
    cards: []
  },

  onShow() {
    this.loadStreetAreas();
    this.loadRecommendations();
  },

  loadStreetAreas() {
    request({ url: '/api/street-areas' })
      .then((res) => {
        const list = (res && res.data) || [];
        const streetAreas = list.map((it) => ({
          id: it.id,
          name: it.name,
          status: it.status,
          cover: resolveImageUrl(it.coverImageUrl)
        }));
        this.setData({ streetAreas });
      })
      .catch(toastError);
  },

  // "今日去哪"推荐：每次进入首页实时计算（后端规则评分，不依赖登录）
  loadRecommendations() {
    request({ url: '/api/recommendations/today' })
      .then((res) => {
        const cards = ((res && res.cards) || []).map((it) => ({
          id: it.id,
          type: it.type,
          targetId: it.targetId,
          title: it.title,
          subtitle: it.subtitle,
          reason: it.reason,
          hasCoupon: it.hasCoupon,
          cover: resolveImageUrl(it.coverImageUrl)
        }));
        this.setData({ cards });
      })
      .catch(() => {
        // 推荐失败不阻塞首页，静默降级为不展示
        this.setData({ cards: [] });
      });
  },

  openRecommendation(e) {
    const card = this.data.cards[e.currentTarget.dataset.index];
    if (!card) return;
    let url = '';
    if (card.type === 'STORE') {
      url = '/pages/store-detail/store-detail?id=' + card.targetId;
    } else if (card.type === 'ACTIVITY') {
      url = '/pages/activity-detail/activity-detail?id=' + card.targetId;
    } else if (card.type === 'EXPERIENCE_ROUTE') {
      url = '/pages/route-detail/route-detail?id=' + card.targetId;
    }
    if (url) {
      wx.navigateTo({ url });
    }
  },

  openStreet(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/street/street?id=' + id });
  },

  openRoutes() {
    wx.navigateTo({ url: '/pages/route-list/route-list' });
  },

  openCoupons() {
    wx.navigateTo({ url: '/pages/coupon-list/coupon-list' });
  },

  openTasks() {
    wx.navigateTo({ url: '/pages/task-list/task-list' });
  }
});