const { request, toastError } = require('../../utils/request');
const { isLoggedIn } = require('../../utils/auth');

const STATUS_TEXT = {
  AVAILABLE: '可使用',
  REDEEMED: '已核销',
  EXPIRED: '已过期'
};

function fmt(t) {
  return t ? String(t).replace('T', ' ').substring(0, 16) : '';
}

Page({
  data: {
    coupons: []
  },

  onShow() {
    if (!isLoggedIn()) {
      this.setData({ coupons: [] });
      return;
    }
    this.load();
  },

  load() {
    request({ url: '/api/users/me/coupons?size=50', needAuth: true })
      .then((res) => {
        const coupons = ((res && res.data) || []).map((it) => ({
          id: it.id,
          name: it.name,
          discountText: it.discountText,
          storeName: it.storeName,
          validTo: fmt(it.validTo),
          status: it.effectiveStatus || it.status,
          statusText: STATUS_TEXT[it.effectiveStatus || it.status] || it.status
        }));
        this.setData({ coupons });
      })
      .catch(toastError);
  },

  openDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/my-coupon-detail/my-coupon-detail?id=' + id });
  }
});
