import type {LocationQueryRaw, RouteRecordRaw} from 'vue-router';
import {createRouter, createWebHashHistory} from 'vue-router'
import {useUserStore} from "/@/stores";

const routes: RouteRecordRaw[] = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('/@/views/login/index.vue')
    },
    {
        path: '/', name: 'Layout',
        component: () => import('/@/layout/BasicLayout.vue'),
        redirect: '/dashboard',
        children: [
            {path: 'dashboard', name: 'Dashboard', component: () => import('/@/views/dashboard/index.vue')},
            {path: 'demo', name: 'Demo', component: () => import('/@/views/demo/index.vue')},
        ]
    }
]
const router = createRouter({
    history: createWebHashHistory(),
    routes,
    scrollBehavior() {
        return {top: 0};
    },
})
const whiteList = ['/login', '/404'];
router.beforeEach(async (to, _from, next) => {
    const userStore = useUserStore();
    const hasToken = userStore.accessToken;
    if (hasToken) {
        if (to.path === '/login') {
            // 已登录用户访问登录页，重定向到首页
            next({path: '/'});
        } else {
            // 已登录用户访问其他页面
            if (userStore.userInfo && userStore.userInfo.id) {
                // --- 情况 A: 信息已加载，直接放行 ---
                // 可以在这里再次校验权限 (可选，如果做了细粒度控制)
                // if (!hasPermission(to)) { next('/403'); return; }
                next();
            } else {
                // --- 情况 B: 有 Token 但没用户信息 (刷新页面后) ---
                // 2.1 获取用户详细信息 (调用 /auth/info 接口)
                // 注意：如果此时 Access Token 过期，Alova 拦截器会自动尝试刷新 Token
                // 如果刷新成功，这个请求会重试并成功；如果刷新失败，会跳转登录
                try {
                    await userStore.getUserInfo();
                    next({...to, replace: true});
                } catch (e) {
                    // 获取信息失败 (Token 无效或网络错误)
                    // 清除 Token 并跳转登录
                    await userStore.logout();
                    // next(`/login?redirect=${to.path}`);
                    next({
                        name: 'login',
                        query: {
                            redirect: to.name,
                            ...to.query,
                        } as LocationQueryRaw,
                    });
                }
            }
        }
    } else {
        // 3. 未登录
        if (whiteList.includes(to.path)) {
            // 在白名单内，直接放行
            next();
        } else {
            // 不在白名单，重定向到登录页，并带上 redirect 参数
            next(`/login?redirect=${encodeURIComponent(to.fullPath)}`);
        }
    }
});
export default router