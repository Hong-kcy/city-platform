// 管理后台本地会话（Demo 阶段：商户身份，非后端 token）。
// 后端商户/运营鉴权尚未实现，此处仅用 localStorage 保存当前操作的商户身份。
const KEY = 'admin-session';

export function getSession() {
  try {
    return JSON.parse(localStorage.getItem(KEY) || 'null');
  } catch (e) {
    return null;
  }
}

export function setSession(session) {
  localStorage.setItem(KEY, JSON.stringify(session));
}

export function clearSession() {
  localStorage.removeItem(KEY);
}