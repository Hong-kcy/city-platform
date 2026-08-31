const { request, toastError } = require('../../utils/request');

const STATUS_TEXT = {
  ACTIVE: '领取中',
  NOT_STARTED: '未开始',
  EXPIRED: '已过期',
  INACTIVE: '已停用'
};

function fmt(t) {
  return t ? String(t).replace('T', ' ').substring(0, 16) : '';
}

Page({
  data: {
    coupons: []
  },

  onShow() {
    this.load();
  },

  load() {
    request({ url: '/api/coupons?status=ACTIVE&size=50' })
      .then((res) => {
        const coupons = ((res && res.data) || []).map((it) => ({
          id: it.id,
          name: it.name,
          discountText: it.discountText,
          storeName: it.storeName,
          validFrom: fmt(it.validFrom),
          validTo: fmt(it.validTo),
          statusText: STATUS_TEXT[it.effectiveStatus] || it.effectiveStatus
        }));
        this.setData({ coupons });
      })
      .catch(toastError);
  },

  openDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/coupon-detail/coupon-detail?id=' + id });
  },

  goMyCoupons() {
    wx.navigateTo({ url: '/pages/my-coupons/my-coupons' });
  }
});
