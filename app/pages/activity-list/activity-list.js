const { request, resolveImageUrl, toastError } = require('../../utils/request');

const TYPE_TEXT = {
  FESTIVAL: '节庆',
  PERFORMANCE: '演出',
  EXHIBITION: '展览',
  PROMOTION: '促销',
  CULTURE: '文化',
  OTHER: '其他'
};

function fmt(t) {
  return t ? String(t).replace('T', ' ').substring(0, 16) : '';
}

Page({
  data: {
    activities: []
  },

  onShow() {
    this.load();
  },

  load() {
    request({ url: '/api/activities?status=PUBLISHED' })
      .then((res) => {
        const list = (res && res.data) || [];
        this.setData({
          activities: list.map((it) => ({
            id: it.id,
            title: it.title,
            summary: it.summary,
            cover: resolveImageUrl(it.coverImageUrl),
            typeText: TYPE_TEXT[it.activityType] || it.activityType,
            startTime: fmt(it.startTime)
          }))
        });
      })
      .catch(toastError);
  },

  openDetail(e) {
    wx.navigateTo({ url: '/pages/activity-detail/activity-detail?id=' + e.currentTarget.dataset.id });
  }
});