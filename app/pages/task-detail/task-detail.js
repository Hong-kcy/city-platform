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
    myStatus: null,
    // 参与成功后直接展示的任务核销码
    joinInfo: null
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.load();
  },

  load() {
    request({ url: '/api/tasks/' + this.data.id, needAuth: isLoggedIn() })
      .then((res) => {
        if (!res) return Promise.reject({ message: '任务不存在' });
        this.setData({
          detail: {
            id: res.id,
            title: res.title,
            description: res.description,
            storeName: res.storeName,
            rewardValue: res.rewardValue,
            startAt: fmt(res.startAt),
            endAt: fmt(res.endAt)
          },
          myStatus: res.myStatus || null
        });
        if (res.myStatus === 'JOINED') {
          // 已参与：从我的任务里取核销码需要 userTaskId，此处提示去我的任务查看
        }
      })
      .catch(toastError);
  },

  join() {
    ensureLogin()
      .then(() => {
        return request({
          url: '/api/tasks/' + this.data.id + '/join',
          method: 'POST',
          needAuth: true
        });
      })
      .then((res) => {
        // 参与成功：返回含任务核销码的参与记录，直接展示
        this.setData({
          myStatus: 'JOINED',
          joinInfo: {
            id: res.id,
            taskCode: res.taskCode
          }
        });
        wx.showToast({ title: '参与成功', icon: 'success' });
        drawQrcode('taskCanvas', res.taskCode, 180, this);
      })
      .catch(toastError);
  },

  goMyTasks() {
    wx.navigateTo({ url: '/pages/my-tasks/my-tasks' });
  }
});
