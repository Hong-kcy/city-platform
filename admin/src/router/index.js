import { createRouter, createWebHistory } from 'vue-router';
import AdminLayout from '../layout/AdminLayout.vue';
import Login from '../pages/merchant/Login.vue';
import MerchantProfile from '../pages/merchant/MerchantProfile.vue';
import StoreList from '../pages/merchant/StoreList.vue';
import StoreEdit from '../pages/merchant/StoreEdit.vue';
import ActivityList from '../pages/merchant/ActivityList.vue';
import ActivityEdit from '../pages/merchant/ActivityEdit.vue';
import CouponList from '../pages/merchant/CouponList.vue';
import CouponEdit from '../pages/merchant/CouponEdit.vue';
import CouponRedeem from '../pages/merchant/CouponRedeem.vue';
import TaskList from '../pages/merchant/TaskList.vue';
import TaskEdit from '../pages/merchant/TaskEdit.vue';
import TaskComplete from '../pages/merchant/TaskComplete.vue';
import OperationIndex from '../pages/operation/Index.vue';
import RouteList from '../pages/operation/RouteList.vue';
import RouteEdit from '../pages/operation/RouteEdit.vue';
import { getSession } from '../utils/session';

const routes = [
  { path: '/login', name: 'login', component: Login },
  {
    path: '/',
    component: AdminLayout,
    redirect: '/merchant',
    children: [
      { path: 'merchant', name: 'merchant', component: MerchantProfile },
      { path: 'stores', name: 'stores', component: StoreList },
      { path: 'stores/new', name: 'store-new', component: StoreEdit },
      { path: 'stores/:id/edit', name: 'store-edit', component: StoreEdit },
      { path: 'activities', name: 'activities', component: ActivityList },
      { path: 'activities/new', name: 'activity-new', component: ActivityEdit },
      { path: 'activities/:id/edit', name: 'activity-edit', component: ActivityEdit },
      { path: 'coupons', name: 'coupons', component: CouponList },
      { path: 'coupons/new', name: 'coupon-new', component: CouponEdit },
      { path: 'coupons/:id/edit', name: 'coupon-edit', component: CouponEdit },
      { path: 'coupon-redeem', name: 'coupon-redeem', component: CouponRedeem },
      { path: 'tasks', name: 'tasks', component: TaskList },
      { path: 'tasks/new', name: 'task-new', component: TaskEdit },
      { path: 'tasks/:id/edit', name: 'task-edit', component: TaskEdit },
      { path: 'task-complete', name: 'task-complete', component: TaskComplete },
      { path: 'operation', name: 'operation', component: OperationIndex },
      { path: 'routes', name: 'routes', component: RouteList },
      { path: 'routes/new', name: 'route-new', component: RouteEdit },
      { path: 'routes/:id/edit', name: 'route-edit', component: RouteEdit }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Demo 阶段最小登录守卫：仅本地商户身份，未登录跳登录页。
// 商户/运营后端鉴权尚未实现，记录为后续问题。
router.beforeEach((to) => {
  const session = getSession();
  if (to.path !== '/login' && !session) {
    return '/login';
  }
  return true;
});

export default router;