// app.js
App({
  onLaunch() {
    // 登录态由 utils/auth 惰性处理，进入需登录功能时再触发 wx.login
  },
  globalData: {
    userInfo: null
  }
});