/**
 * 二维码生成工具（表现层能力，不涉及任何业务规则）。
 * 实现 QR Code Version 1（21×21）、纠错级别 M、字节模式、掩码 0，
 * 支持最长 14 个 ASCII 字符（核销码 CY/TK + 8 位 = 10 字符，留有余量）。
 * 使用小程序旧版 Canvas 接口绘制，含 4 模块静区。
 */

const N = 21;            // Version 1 尺寸
const DATA_CODEWORDS = 16; // V1-M 数据码字数
const EC_CODEWORDS = 10;   // V1-M 纠错码字数

// ---- GF(256) 运算（本原多项式 0x11D）----
const EXP = new Array(256);
const LOG = new Array(256);
(function initTables() {
  let x = 1;
  for (let i = 0; i < 255; i++) {
    EXP[i] = x;
    LOG[x] = i;
    x <<= 1;
    if (x & 0x100) x ^= 0x11d;
  }
  EXP[255] = EXP[0];
})();

function gmul(a, b) {
  if (a === 0 || b === 0) return 0;
  return EXP[(LOG[a] + LOG[b]) % 255];
}

// ---- Reed-Solomon 纠错码字 ----
function polyMul(a, b) {
  const res = new Array(a.length + b.length - 1).fill(0);
  for (let i = 0; i < a.length; i++) {
    for (let j = 0; j < b.length; j++) {
      res[i + j] ^= gmul(a[i], b[j]);
    }
  }
  return res;
}

function rsRemainder(data) {
  // 生成多项式：∏(x - α^i), i = 0..9
  let gen = [1];
  for (let i = 0; i < EC_CODEWORDS; i++) {
    gen = polyMul(gen, [1, EXP[i]]);
  }
  const res = data.concat(new Array(EC_CODEWORDS).fill(0));
  for (let i = 0; i <= res.length - gen.length; i++) {
    const coef = res[i];
    if (coef !== 0) {
      for (let j = 0; j < gen.length; j++) {
        res[i + j] ^= gmul(gen[j], coef);
      }
    }
  }
  return res.slice(data.length);
}

// ---- 数据编码（字节模式）----
function encodeData(text) {
  const bytes = [];
  for (let i = 0; i < text.length; i++) {
    const c = text.charCodeAt(i);
    if (c > 255) {
      throw new Error('二维码内容仅支持 ASCII 字符');
    }
    bytes.push(c);
  }
  if (bytes.length > 14) {
    throw new Error('二维码内容过长（最长14字符）');
  }
  const bits = [];
  const push = (value, len) => {
    for (let i = len - 1; i >= 0; i--) {
      bits.push((value >> i) & 1);
    }
  };
  push(0b0100, 4);               // 模式：字节
  push(bytes.length, 8);          // 字符数（V1-9 为 8 位）
  bytes.forEach((b) => push(b, 8));
  const capacityBits = DATA_CODEWORDS * 8;
  push(0, Math.min(4, capacityBits - bits.length)); // 终止符
  while (bits.length % 8 !== 0) bits.push(0);        // 字节对齐
  const pad = [0xec, 0x11];
  let pi = 0;
  while (bits.length < capacityBits) {
    push(pad[pi++ % 2], 8);
  }
  const data = [];
  for (let i = 0; i < bits.length; i += 8) {
    let v = 0;
    for (let j = 0; j < 8; j++) v = (v << 1) | bits[i + j];
    data.push(v);
  }
  return data;
}

// ---- 格式信息 BCH(15,5)：纠错级别 M(00) + 掩码 0 ----
function formatBits() {
  return 0x5412; // M/000 的标准格式位串 101010000010010
}

/**
 * 生成 21×21 的模块矩阵（1=黑，0=白）。
 */
function generateMatrix(text) {
  const data = encodeData(text);
  const ec = rsRemainder(data);
  const codewords = data.concat(ec); // 26 码字 = 208 位

  const matrix = Array.from({ length: N }, () => new Array(N).fill(0));
  const reserved = Array.from({ length: N }, () => new Array(N).fill(false));

  // 定位图形 + 分隔符（8×8 区域）
  const placeFinder = (r, c) => {
    for (let i = -1; i <= 7; i++) {
      for (let j = -1; j <= 7; j++) {
        const rr = r + i;
        const cc = c + j;
        if (rr < 0 || rr >= N || cc < 0 || cc >= N) continue;
        const dark = i >= 0 && i <= 6 && j >= 0 && j <= 6 &&
          (i === 0 || i === 6 || j === 0 || j === 6 ||
            (i >= 2 && i <= 4 && j >= 2 && j <= 4));
        matrix[rr][cc] = dark ? 1 : 0;
        reserved[rr][cc] = true;
      }
    }
  };
  placeFinder(0, 0);
  placeFinder(0, N - 7);
  placeFinder(N - 7, 0);

  // 校正图形：Version 1 无

  // 时序图形（第 6 行/列）
  for (let i = 8; i < N - 8; i++) {
    matrix[6][i] = i % 2 === 0 ? 1 : 0;
    reserved[6][i] = true;
    matrix[i][6] = i % 2 === 0 ? 1 : 0;
    reserved[i][6] = true;
  }

  // 格式信息（两份）+ 固定暗模块
  const fmt = formatBits();
  for (let i = 0; i < 15; i++) {
    const mod = ((fmt >> i) & 1) === 1;
    let r;
    let c;
    // 竖排（第 8 列）
    if (i < 6) r = i;
    else if (i < 8) r = i + 1;
    else r = N - 15 + i;
    matrix[r][8] = mod ? 1 : 0;
    reserved[r][8] = true;
    // 横排（第 8 行）
    if (i < 8) c = N - 1 - i;
    else if (i < 9) c = 7;
    else c = 15 - i - 1;
    matrix[8][c] = mod ? 1 : 0;
    reserved[8][c] = true;
  }
  matrix[N - 8][8] = 1; // 固定暗模块
  reserved[N - 8][8] = true;

  // 数据放置：从右下角起，两列一组蛇形填充，跳过第 6 列（时序）
  let bitIdx = 0;
  const nextBit = () => {
    if (bitIdx >= codewords.length * 8) return 0;
    const byte = codewords[bitIdx >> 3];
    const bit = (byte >> (7 - (bitIdx & 7))) & 1;
    bitIdx++;
    return bit;
  };
  let dirUp = true;
  for (let col = N - 1; col > 0; col -= 2) {
    if (col === 6) col--; // 跳过时序列
    for (let k = 0; k < N; k++) {
      const row = dirUp ? N - 1 - k : k;
      for (let c = col; c >= col - 1; c--) {
        if (!reserved[row][c]) {
          const bit = nextBit();
          const mask = (row + c) % 2 === 0 ? 1 : 0; // 掩码 0
          matrix[row][c] = bit ^ mask;
        }
      }
    }
    dirUp = !dirUp;
  }
  return matrix;
}

/**
 * 绘制二维码到小程序 Canvas（含 4 模块静区）。
 * @param canvasId  canvas-id
 * @param text      编码内容（≤14 个 ASCII 字符）
 * @param size      画布边长（px）
 * @param page      页面实例（组件内 canvas 需要传 this）
 */
function drawQrcode(canvasId, text, size, page) {
  const matrix = generateMatrix(text);
  const n = matrix.length;
  const quiet = 4;
  const total = n + quiet * 2;
  const cell = size / total;
  const ctx = wx.createCanvasContext(canvasId, page);
  ctx.setFillStyle('#ffffff');
  ctx.fillRect(0, 0, size, size);
  ctx.setFillStyle('#000000');
  for (let r = 0; r < n; r++) {
    for (let c = 0; c < n; c++) {
      if (matrix[r][c]) {
        ctx.fillRect((quiet + c) * cell, (quiet + r) * cell, cell + 0.5, cell + 0.5);
      }
    }
  }
  ctx.draw();
}

module.exports = {
  generateMatrix,
  drawQrcode
};
