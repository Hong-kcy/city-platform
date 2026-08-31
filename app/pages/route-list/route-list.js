const { request, toastError } = require('../../utils/request');

const THEME_TEXT = {
  FRIEND_PHOTO: '闺蜜出片',
  SOLO_RELAX: '社恐友好',
  FAMILY_FUN: '亲子放电',
  SLOW_WALK: '周末慢逛',
  OTHER: '其他'
};

function durationText(minutes) {
  if (!minutes) return '';
  if (minutes < 60) return '约' + minutes + '分钟';
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  return m === 0 ? '约' + h + '小时' : '约' + h + '小时' + m + '分钟';
}

Page({
  data: {
    routes: []
  },

  onShow() {
    this.load();
  },

  load() {
    request({ url: '/api/experience-routes?status=ACTIVE' })
      .then((res) => {
        const list = (res && res.data) || [];
        this.setData({
          routes: list.map((it) => ({
            id: it.id,
            name: it.name,
            themeText: THEME_TEXT[it.theme] || it.theme,
            durationText: durationText(it.estimatedDuration),
            poiCount: it.poiCount,
            streetAreaName: it.streetAreaName
          }))
        });
      })
      .catch(toastError);
  },

  openDetail(e) {
    wx.navigateTo({ url: '/pages/route-detail/route-detail?id=' + e.currentTarget.dataset.id });
  }
});
