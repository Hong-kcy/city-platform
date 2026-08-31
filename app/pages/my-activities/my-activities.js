const { request, resolveImageUrl, toastError } = require('../../utils/request');
const { ensureLogin } = require('../../utils/auth');

function fmt(t) {
  return t ? String(t).replace('T', ' ').substring(0, 16) : '';
}

Page({
  data: {
    items: []
  },

  onLoad() {
    this.load();
  },

  load() {
    ensureLogin()
      .then(() => request({ url: '/api/users/me/activity-subscriptions', needAuth: true }))
      .then((res) => {
        const list = (res && res.data) || [];
        this.setData({
          items: list.map((it) => ({
            activityId: it.activityId,
            title: it.title,
            startTime: fmt(it.startTime),
            activityStatus: it.activityStatus,
            cover: resolveImageUrl(it.coverImageUrl)
          }))
        });
      })
      .catch(toastError);
  },

  openDetail(e) {
    wx.navigateTo({ url: '/pages/activity-detail/activity-detail?id=' + e.currentTarget.dataset.id });
  }
});