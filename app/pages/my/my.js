const { request, resolveImageUrl, toastError } = require('../../utils/request');
const { isLoggedIn, ensureLogin, clearToken } = require('../../utils/auth');

Page({
  data: {
    loggedIn: false,
    user: null,
    editNickname: ''
  },

  onShow() {
    const loggedIn = isLoggedIn();
    this.setData({ loggedIn });
    if (loggedIn) {
      this.loadMe();
    } else {
      this.setData({ user: null, editNickname: '' });
    }
  },

  loadMe() {
    request({ url: '/api/users/me', needAuth: true })
      .then((res) => {
        const user = Object.assign({}, res, { avatar: resolveImageUrl(res.avatarUrl) });
        this.setData({ user, editNickname: res.nickname || '' });
      })
      .catch((err) => {
        if (err && err.statusCode === 401) {
          clearToken();
          this.setData({ loggedIn: false, user: null });
        } else {
          toastError(err);
        }
      });
  },

  login() {
    ensureLogin()
      .then(() => {
        this.setData({ loggedIn: true });
        this.loadMe();
      })
      .catch(toastError);
  },

  logout() {
    clearToken();
    this.setData({ loggedIn: false, user: null, editNickname: '' });
  },

  onNicknameInput(e) {
    this.setData({ editNickname: e.detail.value });
  },

  saveProfile() {
    request({
      url: '/api/users/me',
      method: 'PUT',
      data: { nickname: this.data.editNickname },
      needAuth: true
    })
      .then((res) => {
        const user = Object.assign({}, res, { avatar: resolveImageUrl(res.avatarUrl) });
        this.setData({ user, editNickname: res.nickname || '' });
        wx.showToast({ title: '已保存', icon: 'success' });
      })
      .catch(toastError);
  },

  onReminderChange(e) {
    this.updatePreference({ activityReminderEnabled: e.detail.value });
  },

  onNotificationChange(e) {
    this.updatePreference({ systemNotificationEnabled: e.detail.value });
  },

  updatePreference(body) {
    request({ url: '/api/users/me', method: 'PUT', data: body, needAuth: true })
      .then((res) => {
        const user = Object.assign({}, res, { avatar: resolveImageUrl(res.avatarUrl) });
        this.setData({ user });
      })
      .catch(toastError);
  },

  goMyActivities() {
    wx.navigateTo({ url: '/pages/my-activities/my-activities' });
  },

  goMyCoupons() {
    wx.navigateTo({ url: '/pages/my-coupons/my-coupons' });
  },

  goMyTasks() {
    wx.navigateTo({ url: '/pages/my-tasks/my-tasks' });
  }
});