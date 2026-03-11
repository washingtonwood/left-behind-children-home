/**
 * 守护童行 - 前端工具函数库
 */

// ==================== API 配置 ====================
const API_BASE = 'http://localhost:8080/api';

// ==================== API 请求封装 ====================
/**
 * 发送 API 请求
 * @param {string} url - 请求URL
 * @param {object} options - 请求选项
 * @returns {Promise} Promise对象
 */
async function fetchAPI(url, options = {}) {
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const config = { ...defaultOptions, ...options };

    try {
        const response = await fetch(`${API_BASE}${url}`, config);
        const data = await response.json();

        if (data.code === 0 || response.ok) {
            return data;
        } else {
            throw new Error(data.message || '请求失败');
        }
    } catch (error) {
        console.error('API请求错误:', error);
        throw error;
    }
}

/**
 * GET 请求
 */
function get(url, params = {}) {
    const queryString = new URLSearchParams(params).toString();
    const fullUrl = queryString ? `${url}?${queryString}` : url;
    return fetchAPI(fullUrl);
}

/**
 * POST 请求
 */
function post(url, data) {
    return fetchAPI(url, {
        method: 'POST',
        body: JSON.stringify(data),
    });
}

// ==================== Toast 提示组件 ====================
let toastContainer = null;

/**
 * 显示 Toast 提示
 * @param {string} message - 提示消息
 * @param {string} type - 提示类型: success, error, warning, info
 * @param {number} duration - 显示时长(毫秒)
 */
function showToast(message, type = 'info', duration = 3000) {
    if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.id = 'toast-container';
        toastContainer.style.cssText = `
            position: fixed;
            top: 100px;
            right: 20px;
            z-index: 9999;
            display: flex;
            flex-direction: column;
            gap: 10px;
        `;
        document.body.appendChild(toastContainer);
    }

    const toast = document.createElement('div');
    const colors = {
        success: 'bg-green-500',
        error: 'bg-red-500',
        warning: 'bg-yellow-500',
        info: 'bg-blue-500'
    };

    const icons = {
        success: '✓',
        error: '✕',
        warning: '⚠',
        info: 'ℹ'
    };

    toast.className = `${colors[type]} text-white px-6 py-4 rounded-lg shadow-lg flex items-center space-x-3 transform transition-all duration-300`;
    toast.style.cssText = `
        animation: toastSlideIn 0.3s ease-out;
        min-width: 300px;
    `;
    toast.innerHTML = `
        <span class="text-xl">${icons[type]}</span>
        <span class="flex-1">${message}</span>
        <button onclick="this.parentElement.remove()" class="text-white hover:text-gray-200 ml-2">✕</button>
    `;

    toastContainer.appendChild(toast);

    // 自动移除
    setTimeout(() => {
        toast.style.animation = 'toastSlideOut 0.3s ease-in forwards';
        setTimeout(() => toast.remove(), 300);
    }, duration);
}

// ==================== Loading 组件 ====================
let loadingElement = null;

/**
 * 显示 Loading
 * @param {string} text - 提示文字
 */
function showLoading(text = '加载中...') {
    if (loadingElement) return;

    loadingElement = document.createElement('div');
    loadingElement.id = 'loading-overlay';
    loadingElement.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 10000;
    `;
    loadingElement.innerHTML = `
        <div class="bg-white rounded-2xl p-8 flex flex-col items-center space-y-4 shadow-2xl">
            <div class="w-12 h-12 border-4 border-orange-200 border-t-orange-600 rounded-full animate-spin"></div>
            <p class="text-gray-700 font-medium">${text}</p>
        </div>
    `;
    document.body.appendChild(loadingElement);
}

/**
 * 隐藏 Loading
 */
function hideLoading() {
    if (loadingElement) {
        loadingElement.remove();
        loadingElement = null;
    }
}

// ==================== 表单验证工具 ====================
/**
 * 验证手机号
 */
function isValidPhone(phone) {
    return /^1[3-9]\d{9}$/.test(phone);
}

/**
 * 验证邮箱
 */
function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

/**
 * 验证金额
 */
function isValidAmount(amount) {
    return /^\d+(\.\d{1,2})?$/.test(amount) && parseFloat(amount) > 0;
}

// ==================== 数字格式化 ====================
/**
 * 格式化金额
 */
function formatMoney(amount) {
    return `¥${parseFloat(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
}

/**
 * 格式化百分比
 */
function formatPercent(value) {
    return `${parseFloat(value).toFixed(1)}%`;
}

/**
 * 格式化日期
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diff = now - date;
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));

    if (days === 0) return '今天';
    if (days === 1) return '昨天';
    if (days < 7) return `${days}天前`;
    return date.toLocaleDateString('zh-CN');
}

// ==================== 本地存储工具 ====================
/**
 * 保存到本地存储
 */
function saveToLocal(key, value) {
    try {
        localStorage.setItem(key, JSON.stringify(value));
    } catch (e) {
        console.error('保存到本地存储失败:', e);
    }
}

/**
 * 从本地存储读取
 */
function getFromLocal(key) {
    try {
        const value = localStorage.getItem(key);
        return value ? JSON.parse(value) : null;
    } catch (e) {
        console.error('从本地存储读取失败:', e);
        return null;
    }
}

/**
 * 从本地存储删除
 */
function removeFromLocal(key) {
    try {
        localStorage.removeItem(key);
    } catch (e) {
        console.error('从本地存储删除失败:', e);
    }
}

// ==================== 防抖和节流 ====================
/**
 * 防抖函数
 */
function debounce(func, delay = 300) {
    let timer = null;
    return function (...args) {
        if (timer) clearTimeout(timer);
        timer = setTimeout(() => {
            func.apply(this, args);
        }, delay);
    };
}

/**
 * 节流函数
 */
function throttle(func, delay = 300) {
    let lastTime = 0;
    return function (...args) {
        const now = Date.now();
        if (now - lastTime >= delay) {
            lastTime = now;
            func.apply(this, args);
        }
    };
}

// ==================== 导出工具对象 ====================
window.utils = {
    fetchAPI,
    get,
    post,
    showToast,
    showLoading,
    hideLoading,
    isValidPhone,
    isValidEmail,
    isValidAmount,
    formatMoney,
    formatPercent,
    formatDate,
    saveToLocal,
    getFromLocal,
    removeFromLocal,
    debounce,
    throttle
};

console.log('工具函数库已加载');
