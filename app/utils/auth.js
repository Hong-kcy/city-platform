const { request } = require('./request');

const TOKEN_KEY = 'token';

function getToken() {
  return wx.getStorageSync(TOKEN_KEY) || '';
}

function setToken(token) {
  wx.setStorageSync(TOKEN_KEY, token);
}

function clearToken() {
  wx.removeStorageSync(TOKEN_KEY);
}

function isLoggedIn() {
  return !!getToken();
}

/**
 * 确保已登录：本地有 token 则直接返回；否则走微信官方登录链路
 * wx.login → code → POST /api/auth/wechat/login → 平台 token。
 * 说明：真实微信环境下需后端配置 AppID/AppSecret；未配置时后端返回
 * 明确业务错误，此时调用方捕获后可提示"登录服务未配置"。
 */
function ensureLogin() {
  return new Promise((resolve, reject) => {
    const token = getToken();
    if (token) {
      resolve(token);
      return;
    }
    wx.login({
      success(res) {
        if (!res.code) {
          reject({ message: '微信登录失败：未获取到 code' });
          return;
        }
        request({
          url: '/api/auth/wechat/login',
          method: 'POST',
          data: { code: res.code }
        }).then((data) => {
          setToken(data.token);
          resolve(data.token);
        }).catch(reject);
      },
      fail() {
        reject({ message: '微信登录失败，请稍后重试' });
      }
    });
  });
}

module.exports = {
  TOKEN_KEY,
  getToken,
  setToken,
  clearToken,
  isLoggedIn,
  ensureLogin
};