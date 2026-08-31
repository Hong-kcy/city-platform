const { API_BASE_URL } = require('../config');

/**
 * 统一请求层。
 * 负责拼接 API_BASE_URL、附加登录 token、统一错误处理。
 * 后台返回的图片地址形如 /uploads/xxx 或 /static/xxx，
 * 小程序 img 需要完整 URL，故提供 resolveImageUrl 统一拼接。
 */
function request(options) {
  const { url, method = 'GET', data, needAuth = false, header = {} } = options || {};
  return new Promise((resolve, reject) => {
    const token = needAuth ? wx.getStorageSync('token') : '';
    const headers = { 'content-type': 'application/json' };
    if (token) {
      headers['Authorization'] = 'Bearer ' + token;
    }
    wx.request({
      url: API_BASE_URL + url,
      method,
      data,
      header: Object.assign(headers, header),
      success(res) {
        if (res.statusCode >= 200 && res.statusCode < 300) {
          resolve(res.data);
        } else {
          const body = res.data || {};
          reject({
            statusCode: res.statusCode,
            code: body.code,
            message: body.message || ('请求失败(' + res.statusCode + ')'),
            detail: body
          });
        }
      },
      fail(err) {
        reject({ network: true, message: '网络请求失败，请确认后端已启动', detail: err });
      }
    });
  });
}

/**
 * 把后端返回的相对图片路径补全为可访问的完整 URL。
 */
function resolveImageUrl(path) {
  if (!path) return '';
  if (/^https?:\/\//.test(path)) return path;
  return API_BASE_URL + path;
}

/**
 * 统一 toast 错误提示。
 */
function toastError(err) {
  wx.showToast({
    title: (err && err.message) || '操作失败',
    icon: 'none',
    duration: 2500
  });
}

module.exports = {
  request,
  resolveImageUrl,
  toastError
};