const { request, toastError } = require('../../utils/request');
const { drawQrcode } = require('../../utils/qrcode');

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
    id: null,
    detail: null
  },

  onLoad(options) {
    this.setData({ id: options.id });
  },

  onShow() {
    this.load();
  },

  load() {
    request({ url: '/api/users/me/coupons/' + this.data.id, needAuth: true })
      .then((res) => {
        if (!res) return Promise.reject({ message: '优惠券不存在' });
        const status = res.effectiveStatus || res.status;
        this.setData({
          detail: {
            id: res.id,
            name: res.name,
            discountText: res.discountText,
            description: res.description,
            storeName: res.storeName,
            validFrom: fmt(res.validFrom),
            validTo: fmt(res.validTo),
            claimedAt: fmt(res.claimedAt),
            redeemedAt: fmt(res.redeemedAt),
            redeemCode: status === 'AVAILABLE' ? res.redeemCode : '',
            status: status,
            statusText: STATUS_TEXT[status] || status
          }
        });
        if (status === 'AVAILABLE' && res.redeemCode) {
          drawQrcode('redeemCanvas', res.redeemCode, 180, this);
        }
      })
      .catch(toastError);
  }
});
