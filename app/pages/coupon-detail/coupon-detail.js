const { request, toastError } = require('../../utils/request');
const { isLoggedIn, ensureLogin } = require('../../utils/auth');
const { drawQrcode } = require('../../utils/qrcode');

function fmt(t) {
  return t ? String(t).replace('T', ' ').substring(0, 16) : '';
}

Page({
  data: {
    id: null,
    detail: null,
    claimed: false,
    // 领取成功后直接展示的核销信息
    redeemInfo: null
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.load();
  },

  load() {
    request({ url: '/api/coupons/' + this.data.id, needAuth: isLoggedIn() })
      .then((res) => {
        if (!res) return Promise.reject({ message: '优惠券不存在' });
        this.setData({
          detail: {
            id: res.id,
            name: res.name,
            discountText: res.discountText,
            description: res.description,
            storeName: res.storeName,
            merchantName: res.merchantName,
            validFrom: fmt(res.validFrom),
            validTo: fmt(res.validTo)
          },
          claimed: res.claimed === true
        });
      })
      .catch(toastError);
  },

  claim() {
    ensureLogin()
      .then(() => {
        return request({
          url: '/api/coupons/' + this.data.id + '/claim',
          method: 'POST',
          needAuth: true
        });
      })
      .then((res) => {
        // 领取成功：返回含核销码的领取记录，直接展示
        this.setData({
          claimed: true,
          redeemInfo: {
            redeemCode: res.redeemCode,
            status: res.status,
            id: res.id
          }
        });
        wx.showToast({ title: '领取成功', icon: 'success' });
        this.drawCode(res.redeemCode);
      })
      .catch(toastError);
  },

  drawCode(code) {
    drawQrcode('redeemCanvas', code, 180, this);
  },

  goMyCoupons() {
    wx.navigateTo({ url: '/pages/my-coupons/my-coupons' });
  }
});
