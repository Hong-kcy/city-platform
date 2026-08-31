const { request, resolveImageUrl, toastError } = require('../../utils/request');
const { isLoggedIn, ensureLogin } = require('../../utils/auth');

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
    id: null,
    detail: null,
    subscribed: false
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.load();
  },

  load() {
    request({ url: '/api/activities/' + this.data.id, needAuth: isLoggedIn() })
      .then((res) => {
        if (!res) return Promise.reject({ message: '活动不存在' });
        this.setData({
          detail: Object.assign({}, res, {
            cover: resolveImageUrl(res.coverImageUrl),
            typeText: TYPE_TEXT[res.activityType] || res.activityType,
            startTime: fmt(res.startTime),
            endTime: fmt(res.endTime)
          }),
          subscribed: res.subscriptionStatus === 'ACTIVE'
        });
      })
      .catch(toastError);
  },

  toggleSub() {
    ensureLogin()
      .then(() => {
        if (this.data.subscribed) {
          this.cancel();
        } else {
          this.subscribe();
        }
      })
      .catch(toastError);
  },

  subscribe() {
    request({ url: '/api/activities/' + this.data.id + '/subscription', method: 'POST', needAuth: true })
      .then(() => {
        this.setData({ subscribed: true });
        wx.showToast({ title: '已加入想去', icon: 'success' });
        this.load();
      })
      .catch(toastError);
  },

  cancel() {
    request({ url: '/api/activities/' + this.data.id + '/subscription', method: 'DELETE', needAuth: true })
      .then(() => {
        this.setData({ subscribed: false });
        wx.showToast({ title: '已取消', icon: 'none' });
      })
      .catch(toastError);
  }
});