const { request, toastError } = require('../../utils/request');

function fmt(t) {
  return t ? String(t).replace('T', ' ').substring(0, 16) : '';
}

Page({
  data: {
    tasks: []
  },

  onShow() {
    this.load();
  },

  load() {
    request({ url: '/api/tasks?status=ACTIVE&size=50' })
      .then((res) => {
        const tasks = ((res && res.data) || []).map((it) => ({
          id: it.id,
          title: it.title,
          storeName: it.storeName,
          rewardValue: it.rewardValue,
          startAt: fmt(it.startAt),
          endAt: fmt(it.endAt)
        }));
        this.setData({ tasks });
      })
      .catch(toastError);
  },

  openDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({ url: '/pages/task-detail/task-detail?id=' + id });
  },

  goMyTasks() {
    wx.navigateTo({ url: '/pages/my-tasks/my-tasks' });
  }
});
