const { request, toastError } = require('../../utils/request');
const { isLoggedIn } = require('../../utils/auth');

function fmt(t) {
  return t ? String(t).replace('T', ' ').substring(0, 16) : '';
}

Page({
  data: {
    tasks: []
  },

  onShow() {
    if (!isLoggedIn()) {
      this.setData({ tasks: [] });
      return;
    }
    this.load();
  },

  load() {
    request({ url: '/api/users/me/tasks?size=50', needAuth: true })
      .then((res) => {
        const tasks = ((res && res.data) || []).map((it) => ({
          id: it.id,
          title: it.title,
          storeName: it.storeName,
          rewardValue: it.rewardValue,
          taskStatus: it.taskStatus,
          statusText: it.taskStatus === 'COMPLETED' ? (it.rewardIssued ? '已完成 · 积分已发放' : '已完成') : '进行中',
          joinedAt: fmt(it.joinedAt),
          completedAt: fmt(it.completedAt)
        }));
        this.setData({ tasks });
      })
      .catch(toastError);
  },

  openDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/my-task-detail/my-task-detail?id=' + id });
  }
});
