const { request, resolveImageUrl, toastError } = require('../../utils/request');

Page({
  data: {
    store: null
  },

  onLoad(options) {
    this.loadStore(options.id);
  },

  loadStore(id) {
    request({ url: '/api/stores/' + id })
      .then((res) => {
        if (!res) return Promise.reject({ message: '门店不存在' });
        this.setData({
          store: Object.assign({}, res, { cover: resolveImageUrl(res.coverImageUrl) })
        });
      })
      .catch(toastError);
  }
});