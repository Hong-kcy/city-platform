const { request, toastError } = require('../../utils/request');
const { drawQrcode } = require('../../utils/qrcode');

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
    request({ url: '/api/users/me/tasks/' + this.data.id, needAuth: true })
      .then((res) => {
        if (!res) return Promise.reject({ message: '任务不存在' });
        const joined = res.taskStatus === 'JOINED';
        this.setData({
          detail: {
            id: res.id,
            title: res.title,
            description: res.description,
            storeName: res.storeName,
            rewardValue: res.rewardValue,
            joinedAt: fmt(res.joinedAt),
            completedAt: fmt(res.completedAt),
            rewardIssued: res.rewardIssued,
            taskStatus: res.taskStatus,
            taskCode: joined ? res.taskCode : '',
            statusText: res.taskStatus === 'COMPLETED'
              ? (res.rewardIssued ? '已完成 · 积分已发放' : '已完成')
              : '进行中'
          }
        });
        if (joined && res.taskCode) {
          drawQrcode('taskCanvas', res.taskCode, 180, this);
        }
      })
      .catch(toastError);
  }
});
