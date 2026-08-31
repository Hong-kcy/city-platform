// 统一请求封装。开发阶段经 Vite 代理同源转发到后端，BASE 为空。
const BASE = '';

async function request({ url, method = 'GET', data, headers = {} }) {
  const opts = {
    method,
    headers: { 'Content-Type': 'application/json', ...headers }
  };
  if (data !== undefined && data !== null) {
    opts.body = JSON.stringify(data);
  }
  const res = await fetch(BASE + url, opts);
  let body = null;
  try {
    body = await res.json();
  } catch (e) {
    // 非 JSON 响应（如静态资源）
  }
  if (!res.ok) {
    const err = new Error((body && body.message) || ('请求失败(' + res.status + ')'));
    err.code = body && body.code;
    err.status = res.status;
    throw err;
  }
  return body;
}

export function uploadFile(file) {
  const fd = new FormData();
  fd.append('file', file);
  return fetch(BASE + '/api/files', { method: 'POST', body: fd })
    .then((res) => res.json())
    .then((json) => {
      if (json && json.url) return json;
      throw new Error((json && json.message) || '上传失败');
    });
}

export function resolveImageUrl(path) {
  if (!path) return '';
  if (/^https?:\/\//.test(path)) return path;
  return path; // 同源相对路径
}

export default request;