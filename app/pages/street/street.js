const { request, resolveImageUrl, toastError } = require('../../utils/request');

Page({
  data: {
    id: null,
    street: null,
    pois: [],
    mapPois: [],
    view: 'list'
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.loadMap();
  },

  loadMap() {
    request({ url: '/api/street-areas/' + this.data.id + '/map' })
      .then((res) => {
        if (!res) return Promise.reject({ message: '街区不存在' });
        const street = {
          id: res.id,
          name: res.name,
          introduction: res.introduction,
          cover: resolveImageUrl(res.coverImageUrl)
        };
        const pois = (res.pois || []).map((p) => ({
          id: p.id,
          name: p.name,
          poiType: p.poiType,
          description: p.description,
          storeId: p.storeId,
          storeName: p.storeName,
          storeBusinessStatus: p.storeBusinessStatus,
          storeCover: resolveImageUrl(p.storeCoverImageUrl),
          longitude: p.longitude,
          latitude: p.latitude
        }));
        this.setData({ street, pois, mapPois: this.buildMap(pois) });
      })
      .catch(toastError);
  },

  // 将 POI 经纬度线性投影到地图容器内的百分比坐标（Demo 无第三方地图 SDK）
  buildMap(pois) {
    const items = pois.filter((p) => p.longitude != null && p.latitude != null);
    if (!items.length) return pois;
    const lons = items.map((p) => Number(p.longitude));
    const lats = items.map((p) => Number(p.latitude));
    const minLon = Math.min.apply(null, lons);
    const maxLon = Math.max.apply(null, lons);
    const minLat = Math.min.apply(null, lats);
    const maxLat = Math.max.apply(null, lats);
    const lonSpread = maxLon - minLon || 1;
    const latSpread = maxLat - minLat || 1;
    const PAD = 10;
    return pois.map((p) => {
      if (p.longitude == null || p.latitude == null) return p;
      const lon = Number(p.longitude);
      const lat = Number(p.latitude);
      const left = PAD + ((lon - minLon) / lonSpread) * (100 - 2 * PAD);
      const top = PAD + ((maxLat - lat) / latSpread) * (100 - 2 * PAD);
      return Object.assign({}, p, { left: left + '%', top: top + '%' });
    });
  },

  switchView(e) {
    this.setData({ view: e.currentTarget.dataset.view });
  },

  tapPoi(e) {
    const { id, storeId, name } = e.currentTarget.dataset;
    if (storeId) {
      wx.navigateTo({ url: '/pages/store-detail/store-detail?id=' + storeId });
    } else {
      wx.showToast({ title: (name || 'POI') + '（景观节点）', icon: 'none' });
    }
  }
});