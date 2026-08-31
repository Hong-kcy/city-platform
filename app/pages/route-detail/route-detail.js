const { request, resolveImageUrl, toastError } = require('../../utils/request');

const THEME_TEXT = {
  FRIEND_PHOTO: '闺蜜出片',
  SOLO_RELAX: '社恐友好',
  FAMILY_FUN: '亲子放电',
  SLOW_WALK: '周末慢逛',
  OTHER: '其他'
};

const POI_TYPE_TEXT = {
  STORE: '门店',
  SCENIC: '景观',
  FACILITY: '设施',
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
    id: null,
    route: null,
    items: []
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.load();
  },

  load() {
    request({ url: '/api/experience-routes/' + this.data.id })
      .then((res) => {
        if (!res) return Promise.reject({ message: '路线不存在' });
        this.setData({
          route: {
            id: res.id,
            name: res.name,
            description: res.description,
            themeText: THEME_TEXT[res.theme] || res.theme,
            durationText: durationText(res.estimatedDuration),
            streetAreaName: res.streetAreaName,
            poiCount: res.poiCount
          },
          items: (res.items || []).map((it) => ({
            sequence: it.sequence,
            poiId: it.poiId,
            poiName: it.poiName,
            poiTypeText: POI_TYPE_TEXT[it.poiType] || it.poiType,
            storeId: it.storeId,
            storeName: it.storeName,
            recommendationReason: it.recommendationReason,
            storeCover: resolveImageUrl(it.storeCoverImageUrl)
          }))
        });
        wx.setNavigationBarTitle({ title: res.name || '路线详情' });
      })
      .catch(toastError);
  },

  // 与街区页 tapPoi 同规则：门店 POI 跳店铺详情，景观节点仅提示
  tapItem(e) {
    const { storeId, poiName } = e.currentTarget.dataset;
    if (storeId) {
      wx.navigateTo({ url: '/pages/store-detail/store-detail?id=' + storeId });
    } else {
      wx.showToast({ title: (poiName || '节点') + '（景观节点）', icon: 'none' });
    }
  }
});
